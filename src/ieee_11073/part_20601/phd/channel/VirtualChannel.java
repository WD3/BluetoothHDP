/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
package ieee_11073.part_20601.phd.channel;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.utils.*;
import ieee_11073.part_20601.asn1.ApduType;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class VirtualChannel {

	private ArrayList<Channel> channels;
	private IFIFO<ApduType> outputQueue;
	private IFIFO<ApduType> inputQueue;
	private IFIFO<Event> eventQueue;
	private boolean open = false;

	private SenderThread sender;
	private Semaphore sem = new Semaphore(0);

	private IUnlock senderController = new IUnlock(){
		public void unlock() {
			sem.release();
		}
	};

	private ChannelEventHandler eventHandler = new ChannelEventHandler(){
		@Override
		public synchronized void processEvent(Event e) {
			int len = channels.size();
			if (e.getTypeOfEvent()!=EventType.IND_TRANS_DESC) {
				eventQueue.add(e);
				return;
			}

			//interrupt all threads blocked in input channels
			for (int i=0; i < len; i++){
				channels.get(i).setReceiverStatus(false);
			}
			//interrupt sender thread
			sender.interrupt();
			//Send disconnected event to fsm
			eventQueue.add(e);
		}
	};

	public VirtualChannel (IFIFO<ApduType> inputQueue, IFIFO<ApduType> outputQueue, IFIFO<Event> eventQueue) {

		channels = new ArrayList<Channel>();
		this.eventQueue = eventQueue;
		this.outputQueue = outputQueue;
		this.inputQueue = inputQueue;
		this.outputQueue.setHandler(senderController);

		/*sender = new SenderThread();
		sender.start();*/
	}

	public void addChannel (Channel chan) {
		int size = channels.size();

		if (size == 0){
			sender = new SenderThread();
			sender.start();
			open = false;
		}

		try {
			channels.add(chan);
			chan.configureChannel(size == 0, this.inputQueue, this.eventHandler);
			if (!open) {
				open = true;
				//VirtualChannel is ready to send and receive APDUs
				eventQueue.add(new Event(EventType.IND_TRANS_CONN));
			}
		} catch (InitializedException e) {
			e.printStackTrace();
		}
	}

	public void delChannel (Channel chan) {
		int size = channels.size();
		Channel channel;
		for (int i = 0; i < size; i++) {
			channel = channels.get(i);
			if (channel.getChannelId() != chan.getChannelId())
				continue;
			channel.setReceiverStatus(false);
			channels.remove(i);
			break;
		}
	}

	/*
	public void configureChannels(IFIFO<ApduType> inputQueue, IFIFO<ApduType> outputQueue, IFIFO<Event> eventQueue) throws InitializedException {
		int len = channels.size();
		if (initialized)
			throw new InitializedException("VirtualChannel is already initialized.");

		Logging.debug("Canal virtual configurado");
		this.eventQueue = eventQueue;
		this.outputQueue = outputQueue;
		this.inputQueue = inputQueue;
		this.outputQueue.setHandler(senderController);

		for (int i=0; i < len; i++){
			channels.get(i).configureChannel(i,inputQueue,eventHandler);
		}
		sender = new SenderThread();
		sender.start();

		initialized=true;
	}
	*/

	public void freeChannels (){
		int len = channels.size();
		for (int i=0; i < len; i++){
			channels.get(i).setReceiverStatus(false);
		}
	}

	public void sendApdu (ApduType apdu) throws Exception {
		int channel;
		if (channels.size() == 1)
			channel = 0;
		else {
			channel = apdu.getChannel();
			if (channel < 0) {
				Logging.debug("TODO: The APDU can be sended for other channel (not primary)");
				/* Not preferences are setted */
				channel = 0;
			}
		}
		channels.get(0).sendAPDU(apdu);
	}

	public class SenderThread extends Thread {

		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					sem.acquire();
					sendApdu(outputQueue.remove());
				} catch (InterruptedException e) {
					Logging.debug("Interrupted sender");
					repeat = false;
				}catch (Exception e) {
					Logging.debug("Exception sender thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			Logging.debug("Exiting sender");
		}
	}
}
