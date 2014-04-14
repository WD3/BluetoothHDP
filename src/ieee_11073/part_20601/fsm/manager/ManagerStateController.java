/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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
package ieee_11073.part_20601.fsm.manager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import es.libresoft.openhealth.Specialization;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.utils.IFIFO;
import es.libresoft.openhealth.utils.IUnlock;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.fsm.State;
import ieee_11073.part_20601.fsm.StateController;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.phd.channel.InitializedException;
import ieee_11073.part_20601.phd.dim.DimTimeOut;
import ieee_11073.part_20601.phd.dim.IMDS_Handler;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.MDS;
import ieee_11073.part_20601.phd.dim.TimeOut;
import ieee_11073.part_20601.phd.dim.manager.MDSManager;


public class ManagerStateController implements StateController {

	private State state;
	private Semaphore semInputs = new Semaphore(0);
	private Semaphore semEvents = new Semaphore(0);

	private IFIFO<ApduType> inputQueue;
	private IFIFO<ApduType> outputQueue;
	private IFIFO<Event> eventQueue;

	private DispatcherApduThread dispatcher;
	private DispatcherEventThread dispatcherEvents;

	private IMDS_Handler mdsHandler;
	//Caching of the system_id

	private boolean initialized = false;
	private Timer timer;

	private ArrayList<DimTimeOut> timeouts = new ArrayList<DimTimeOut>();

	private StateHandler state_handler = new StateHandler(){

		@Override
		public void changeState(State newState) {
			InternalEventReporter.agentChangeStatus(
					(Agent) getMDS().getDevice(), newState.getStateCode(), newState.getStateName());
			state = newState;
		}

		@Override
		public MDS getMDS() {
			return mdsHandler.getMDS();
		}

		@Override
		public void send(ApduType apdu) {
			outputQueue.add(apdu);
			Logging.debug("<<<<<<<<<<<<<<<<<<<<:"+apdu.getName("MDER"));
		}

		@Override
		public void setMDS(MDS newMds) {
			mdsHandler.setMDS(newMds);
		}

		@Override
		public void sendEvent(Event event) {
			eventQueue.add(event);
		}

		@Override
		public synchronized void addTimeout(TimeOut to) {
			timer.purge();
			timer.schedule(to, to.getTimeout());

			if (to instanceof DimTimeOut) {
				timeouts.add((DimTimeOut) to);
			}
		}

		@Override
		public synchronized void removeTimeout(TimeOut to) {
			to.cancel();

			if (to instanceof DimTimeOut) {
				timeouts.remove(to);
			}
		}

		@Override
		public synchronized DimTimeOut retireTimeout(int invokeId) {
			for (int i = 0; i < timeouts.size(); i++) {
				DimTimeOut to = timeouts.get(i);
				if (to.getInvokeId() == invokeId) {
					to.cancel();
					timeouts.remove(i);
					return to;
				}
			}
			return null;
		}

	};

	private IUnlock dispatcherController = new IUnlock(){
		public void unlock() {
			semInputs.release();
		}
	};

	private IUnlock eventController = new IUnlock(){
		public void unlock() {
			semEvents.release();
		}
	};

	public ManagerStateController (IMDS_Handler handler) {
		mdsHandler = handler;
		timer = new Timer();
		this.state = new MDisconnected(state_handler);
	}
	
	public void generateMDS(Specialization spec){
		MDSManager mds = new MDSManager(spec.getSystem_id(), spec.getDev_config_id());
		mds.setStateHandler(state_handler);
		//Set device config
		mds.setDeviceConfig(spec.getDev_conf());
		//Set MDS Object
		try {
			mds.configureMDS(spec.getConfigdata());
		} catch (InvalidAttributeException e) {
			e.printStackTrace();
		}
		state_handler.setMDS(mds);
	}

	public void configureController(IFIFO<ApduType> inputQueue, IFIFO<ApduType> outputQueue, IFIFO<Event> eventQueue){
		//dev_handler = handler;
		this.eventQueue = eventQueue;
		this.eventQueue.setHandler(eventController);
		this.inputQueue = inputQueue;
		this.inputQueue.setHandler(dispatcherController);
		this.outputQueue = outputQueue;
	}

	public void initFSMController() throws InitializedException{
		if (!initialized){
			dispatcher = new DispatcherApduThread();
			dispatcher.start();
			dispatcherEvents = new DispatcherEventThread();
			dispatcherEvents.start();
			initialized = true;
		}else
			throw new InitializedException("Manager state controller is already initialized.");
	}

	public void freeResources (){
		dispatcher.interrupt();
		dispatcherEvents.interrupt();
		timer.cancel();
	}
	public void processApdu (ApduType apdu){
		inputQueue.add(apdu);
	}

	public void processEvent(Event event) {
		eventQueue.add(event);
	}

	public int getStateCode() {
		return state.getStateCode();
	}

	public String getStateName() {
		return state.getStateName();
	}


	/**
	 * Receiver thread for the input channel
	 * @author sancane
	 */
	public class DispatcherApduThread extends Thread {
		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					semInputs.acquire();
					//Send input ADPU to finite state machine
					state.process(inputQueue.remove());
				} catch (InterruptedException e1) {
					Logging.debug("Interrupted dispatcher Apdu thread");
					repeat = false;
				}catch (Exception e) {
					Logging.debug("Exception dispatcher Apdu thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			Logging.debug("Exiting dispatcher");
		}
	}

	/**
	 * Receiver thread for events input channel
	 * @author sancane
	 */
	public class DispatcherEventThread extends Thread {
		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					semEvents.acquire();
					Event event = eventQueue.remove();
					//Send input Event to finite state machine
					boolean result = state.processEvent(event);
					if (!result && event instanceof ExternalEvent) {
						((ExternalEvent<?, ?>) event).processed(null,
								ErrorCodes.INVALID_ACTION);
					}
				} catch (InterruptedException e1) {
					Logging.debug("Interrupted dispatcher Events thread");
					repeat = false;
				}catch (Exception e) {
					Logging.debug("Exception dispatcher Events thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			Logging.debug("Exiting dispatcher event");
		}
	}
}
