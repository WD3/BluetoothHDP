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

import ieee_11073.part_20601.asn1.ApduType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

import org.bn.CoderFactory;
import org.bn.IDecoder;
import org.bn.IEncoder;

import Config.ManagerConfig;
import es.libresoft.openhealth.Device;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventAPDUOverflow;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.IFIFO;

public abstract class Channel {
	private InputStream input;
	private OutputStream output;
	private boolean primary;

	private IDecoder decoder;
	private IEncoder<ApduType> encoder;

	private IFIFO<ApduType> inputQueue;
	private ChannelEventHandler eventHandler;

	private ReceiverThread receiver;

	private boolean initialized = false;

	private Semaphore repeatSem = new Semaphore(1);
	private boolean repeat = true;

	public Channel (InputStream input, OutputStream output) throws Exception {
		this.input = input;
		this.output = output;
		//Set default encoding rules to MDER
		decoder = CoderFactory.getInstance().newDecoder(Device.MDER_DEFAULT);
		encoder = CoderFactory.getInstance().newEncoder(Device.MDER_DEFAULT);
	}

	public synchronized void configureChannel (boolean primary, IFIFO<ApduType> inputQueue, ChannelEventHandler eventHandler) throws InitializedException {
		if (initialized)
			throw new InitializedException ("Channel is already initialized");
		this.primary = primary;
		this.eventHandler = eventHandler;
		this.inputQueue = inputQueue;

		receiver = new ReceiverThread();
		receiver.start();
		initialized = true;
	}

	public synchronized void sendAPDU (ApduType apdu) throws Exception {
		if (!initialized)
			throw new InitializedException ("Channel is not initialized");
		encoder.encode(apdu, output);
	}


	public void setReceiverStatus (boolean status) {
		try {
			repeatSem.acquire();
			this.repeat = status;
			if (!this.repeat && !receiver.isInterrupted()) {
				receiver.interrupt();
			}
		} catch (InterruptedException e) {
			Logging.debug("Interrupted receiver (" + this.getChannelId() + ")");
		} finally {
			repeatSem.release();
		}
	}

	private boolean shouldRepeat () {
		boolean r = false;
		try {
			repeatSem.acquire();
			r = this.repeat;
		} catch (InterruptedException e) {
			Logging.debug("Interrupted receiver (" + this.getChannelId() + ")");
		} finally {
			repeatSem.release();
		}
		return r;
	}


	/**
	 * Receiver thread for the input channel
	 * @author sancane
	 */

	public class ReceiverThread extends Thread {
		public void run() {
			int id = getChannelId();
			ApduType recvApdu;
			while(shouldRepeat ()){
		 		try {
					byte[] buffer = readApdu();
					ByteArrayInputStream is = new ByteArrayInputStream(buffer);
					recvApdu = decoder.decode(is, ApduType.class);
					recvApdu.setChannel(id);
					if (buffer.length > ManagerConfig.A2M_MAX_SIZE){
						Logging.error("Maximum APDU size has been exceeded");
						Event event = new EventAPDUOverflow(EventType.REC_APDU_OVERFLOW, recvApdu);
						eventHandler.processEvent(event);
					}else{
						Logging.debug(">>>>>>>>>>>>>>>>>>>>:"+recvApdu.getName("MDER"));
						inputQueue.add(recvApdu);
					}
				}catch (InterruptedException e) {
					Logging.debug("Interrupted receiver (" + id + ")");
				}catch (NullPointerException e) {
					Logging.error("Corrupted APDUType received");
					eventHandler.processEvent(new Event(EventType.REC_CORRUPTED_APDU));
					Logging.debug("Flushing buffer");
					byte b[] = new byte[ManagerConfig.A2M_MAX_SIZE];
					try {
						int r = input.read(b);
						Logging.debug("Freed " + r + "bytes");
					} catch (IOException e1) {
						e1.printStackTrace();
						if (primary)
							eventHandler.processEvent(new Event(EventType.IND_TRANS_DESC));
					}
				}catch (Exception e) {
					//EOF readed because channel is closed
					if (primary)
						eventHandler.processEvent(new Event(EventType.IND_TRANS_DESC));
				}
			}
			Logging.debug("Receiver thread exiting (" + id + ").");
			releaseChannel();
		}
	}

	private int decodeLength(byte[] buff, int offset) {
		int value = 0;

		for(int i = offset; i < 2 + offset; i++) {
			int bt = buff[i];
			if (bt < 0){
				bt = bt + 256;
			}
			value = (value << 8) | bt;
		}
		return value;
	}

	private byte[] readApdu() throws IOException, Exception{
		byte[] buffer = new byte[4];
		byte[] bapdu;
		int apdusize, length = 0;

		while (length < 4){
			int read;
			read = input.read(buffer, length, 4 - length);
			if (read < 0)
				throw new IOException("IOException while reading from the data channel.");

			length += read;
		}

		apdusize = decodeLength(buffer, 2);
		if (apdusize == 0)
			return buffer;
		if (apdusize < 0){
			throw new IOException("Apdu size can't be zero or a negative number");
		}
		bapdu = new byte[apdusize + 4];

		// An APDU starts with a Choice codification. We must read four bytes to
		// know the APDU type and the length of the data.

		for (int i = 0; i < 4; i++){
			bapdu[i] = buffer[i];
		}
		length = 4;

		do {
			int read;

			read = input.read(bapdu, length, 4 + apdusize-length);
			if (read < 0)
				throw new IOException();
			length += read;
		}
		while (length < apdusize + 4);

		//Logging.error(ASN1_Tools.getHexString(bapdu));
		return bapdu;
	}

	/**
	 * Free resources taken by this channel
	 */
	public abstract void releaseChannel();


	public abstract int getChannelId();
}
