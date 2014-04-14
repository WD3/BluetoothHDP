/*
Copyright (C) 2008-2012 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>
Author: Jorge Fernandez-Gonzalez <jfernandez@libresoft.es>

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

package es.libresoft.openhealth.android;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.InstNumber;
import ieee_11073.part_20601.asn1.OperationalState;
import ieee_11073.part_20601.asn1.SegmSelection;
import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;
import ieee_11073.part_20601.phd.channel.hdp.HDPManagerChannel;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DIM;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.PM_Segment;
import ieee_11073.part_20601.phd.dim.PM_Store;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.example.bluetooth.health.R;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.android.aidl.IAgent;
import es.libresoft.openhealth.android.aidl.IAgentService;
import es.libresoft.openhealth.android.aidl.IManagerClientCallback;
import es.libresoft.openhealth.android.aidl.IManagerService;
import es.libresoft.openhealth.android.aidl.IPMStoreService;
import es.libresoft.openhealth.android.aidl.IScannerService;
import es.libresoft.openhealth.android.aidl.IState;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IError;
import es.libresoft.openhealth.android.aidl.types.IOperationalState;
import es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric;
import es.libresoft.openhealth.android.aidl.types.objects.IDIMClass;
import es.libresoft.openhealth.android.aidl.types.objects.IEnumeration;
import es.libresoft.openhealth.android.aidl.types.objects.IMDS;
import es.libresoft.openhealth.android.aidl.types.objects.INumeric;
import es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment;
import es.libresoft.openhealth.android.aidl.types.objects.IPM_Store;
import es.libresoft.openhealth.android.aidl.types.objects.IRT_SA;
import es.libresoft.openhealth.android.aidl.types.objects.IScanner;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.error.ErrorException;
import es.libresoft.openhealth.error.ErrorFactory;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.events.application.GetPmSegmentEventData;
import es.libresoft.openhealth.events.application.GetPmStoreEventData;
import es.libresoft.openhealth.events.application.SetEventData;
import es.libresoft.openhealth.events.application.TrigPMSegmentXferEventData;
import es.libresoft.openhealth.logging.ILogging;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.storage.ConfigStorageFactory;

public class HealthService extends Service {

	/** Registered clients */
	Vector<IManagerClientCallback> clients = new Vector<IManagerClientCallback>();

	private TcpManagerChannel channelTCP;
	private HDPManagerChannel channelHDP;
	private boolean started = false;
	private Vector<Agent> agents = new Vector<Agent>();
	private ILogging log = new ILogging(){

		@Override
		public void error(String str) {
			Log.e("HealthService", str);
		}

		@Override
		public void debug(String str) {
			Log.d("HealthService", str);
		}

		@Override
		public void info(String str) {
			Log.i("HealthService", str);
		}
	};

	/************************************************************
	 * Internal events triggered from manager thread
	 ************************************************************/
	private final InternalEventManager ieManager = new InternalEventManager() {

		@Override
		public void agentChangeState(Agent agent, int stateCode, String stateName) {
			IManagerClientCallback[] cliArray;
			cliArray = clients.toArray(new IManagerClientCallback[0]);
			for (IManagerClientCallback c: cliArray) {
				try {
					c.agentChangeState(new IAgent(agent.getId(), agent.getTransportDesc()), new IState(stateCode, stateName));
				} catch (RemoteException e) {
					clients.removeElement(c);
				}
			}
		}

		@Override
		public void receivedMeasure(Agent agent, MeasureReporter mr) {
			if (!(mr instanceof AndroidMeasureReporter))
				return;
			AndroidMeasureReporter amr = (AndroidMeasureReporter) mr;
			IAgentMetric metric = amr.getMetric();
			IAgent iagent = new IAgent(agent.getId(), agent.getTransportDesc());

			IManagerClientCallback[] cliArray;
			cliArray = clients.toArray(new IManagerClientCallback[0]);
			for (IManagerClientCallback c: cliArray) {
				try {
					c.agentNewMeassure(iagent, metric);
				} catch (RemoteException e) {
					clients.removeElement(c);
				}
			}
		}

		@Override
		public void agentPlugged(Agent agent) {
			agents.add(agent);
			IManagerClientCallback[] cliArray;
			cliArray = clients.toArray(new IManagerClientCallback[0]);
			for (IManagerClientCallback c: cliArray) {
				try {
					c.agentPlugged(new IAgent(agent.getId(), agent.getTransportDesc()));
				} catch (RemoteException e) {
					clients.removeElement(c);
				}
			}
		}

		@Override
		public void agentUnplugged(Agent agent) {
			agents.removeElement(agent);
			IManagerClientCallback[] cliArray;
			cliArray = clients.toArray(new IManagerClientCallback[0]);
			for (IManagerClientCallback c: cliArray) {
				try {
					c.agentUnplugged(new IAgent(agent.getId(), agent.getTransportDesc()));
				} catch (RemoteException e) {
					clients.removeElement(c);
				}
			}
		}

		@Override
		public void error(Agent agent, int errorCode) {
			IAgent iagent = new IAgent(agent.getId(), agent.getTransportDesc());
			IError error;
			try {
				error = new IError(errorCode, ErrorFactory.getDefaultErrorGenerator().error2string(errorCode));
			} catch (ErrorException e) {
				error = new IError(errorCode, HealthService.this.getString(R.string.UNEXPECTED_ERROR));
			}

			IManagerClientCallback[] cliArray;
			cliArray = clients.toArray(new IManagerClientCallback[0]);
			for (IManagerClientCallback c: cliArray) {
				try {
					c.error(iagent, error);
				} catch (RemoteException e) {
					clients.removeElement(c);
				}
			}
		}
	};

	@Override
	public void onCreate() {
		// Set android logging system
		Logging.setDefaultLogGenerator(log);
		//Set the event manager handler to get internal events from the manager thread
		InternalEventReporter.setDefaultEventManager(ieManager);
		//Set target platform to android to report measures using IPC mechanism
		MeasureReporterFactory.setDefaultMeasureReporter(new AndroidMeasureReporter());
		ConfigStorageFactory.setDefaultConfigStorage(new AndroidConfigStorage(this.getApplicationContext()));
		AttributeUtils.setContext(getApplicationContext());
		ErrorFactory.setDefaultErrorGenerator(new AndroidError(this.getApplicationContext()));
		Logging.debug("Service created");
		channelTCP = new TcpManagerChannel(9999);
		channelHDP = new HDPManagerChannel(this.getBaseContext());
		super.onCreate();
	}

	void initTransportLayers() {
		channelTCP.start();
		channelHDP.start();
	}

	void shutdownTransportLayers() {
		channelTCP.finish();
		channelHDP.finish();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!started) {
			initTransportLayers();
			started = true;
		}else {
			channelHDP.reloadAgents();
		}

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		shutdownTransportLayers();
		started = false;
	}

	/**
	 * The IManagerService is defined through IDL
	 */
	private final IManagerService.Stub managerServiceStub = new IManagerService.Stub() {

		@Override
		public void agents(List<IAgent> agentList) throws RemoteException {
			if (agentList == null)
				return;

			for(Agent agent: agents)
				agentList.add(new IAgent(agent.getId(), agent.getTransportDesc()));
		}

		@Override
		public void registerApplication(IManagerClientCallback mc)
				throws RemoteException {
			if (mc != null){
				boolean isAdded = false;
				for (IManagerClientCallback client: clients){
					if (client.asBinder().equals(mc.asBinder())){
						isAdded = true;
						break;
					}
				}
				if (!isAdded)
					clients.add(mc);
			}
		}

		@Override
		public void unregisterApplication(IManagerClientCallback mc)
				throws RemoteException {
			IManagerClientCallback client = null;
			for (IManagerClientCallback auxClient: clients){
				if (auxClient.asBinder().equals(mc.asBinder())){
					client = auxClient;
					break;
				}
			}

			if (client != null)
				clients.removeElement(client);

			if (clients.size() == 0) {
				HealthService.this.stopSelf();
			}
		}

	};

	private Agent getAgent(IAgent agent) {
		for(Agent a : agents) {
			if (a.getId() == agent.getId())
				return a;
		}
		return null;
	}

	private DIM getObject(Agent a, IDIMClass obj) {
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(obj.getHandle()));

		return a.mdsHandler.getMDS().getObject(handle);
	}

	private void parcelAttributes(DIM object, List<IAttribute> attrs, IError error) {
		Hashtable<Integer, Attribute> attList = object.getAttributes();
		for (Integer key: attList.keySet()) {
			Attribute att = attList.get(key);
			IAttribute iAtt = new IAttribute();
			if (!IAttrFactory.getParcelableAttribute(att, iAtt)) {
				error.setErrCode(ErrorCodes.INVALID_ATTRIBUTE);
				setErrorMessage(error);
				return;
			}

			attrs.add(iAtt);
		}

		error.setErrCode(ErrorCodes.NO_ERROR);
		setErrorMessage(error);
	}
	/**
	 * The IAgentService is defined through IDL
	 */
	private final IAgentService.Stub agentServiceStub = new IAgentService.Stub() {

		@Override
		public void connect(IAgent agent) throws RemoteException {
			//Logging.debug("TODO: Connect with the agent");
			channelHDP.connect(getAgent(agent));
		}

		@Override
		public boolean disconnect(IAgent agent, IError error) throws RemoteException {
			Agent a = getAgent(agent);

			if (error == null) {
				error = new IError();
			}

			if (a == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(error);
				return false;
			}

			AndroidExternalEvent<Boolean, Object> ev = new AndroidExternalEvent<Boolean, Object>(EventType.REQ_ASSOC_REL, null);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				error.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(error);
				return false;
			}

			if (ev.wasError()) {
				error.setErrCode(ev.getError());
				setErrorMessage(error);
				return false;
			}

			if (ev.getRspData()) {
				error.setErrCode(ErrorCodes.NO_ERROR);
				setErrorMessage(error);
				return true;
			} else {
				error.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(error);
				return false;
			}
		}

		@Override
		public boolean updateMDS(IAgent agent, IError err) throws RemoteException {
			Agent a = getAgent(agent);

			if (err == null) {
				err = new IError();
			}

			if (a == null) {
				err.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(err);
				return false;
			}

			AndroidExternalEvent<Boolean, Object> ev = new AndroidExternalEvent<Boolean, Object>(EventType.REQ_MDS, null);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return false;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);

			return ev.getRspData();
		}

		@Override
		public void getState(IAgent agent, IState state, IError error)
		{
			Agent a = checkParameters(agent, state, error);
			if (a == null)
				return;

			state.setStateCode(a.getStateCode());
			state.setStateName(a.getStateName());
		}

		@Override
		public void getMDS(IAgent agent, IMDS mds, IError error)
				throws RemoteException {
			Agent a = checkParameters(agent, mds, error);
			if (a == null)
				return;

			IMDS rmds = new IMDS(0, agent);
			mds.copy(rmds);
		}

		@Override
		public void getNumeric(IAgent agent, List<INumeric> nums, IError error)
				throws RemoteException {

			Agent a = checkParameters(agent, nums, error);
			if (a == null)
				return;

			for (Integer handle: a.mdsHandler.getMDS().getNumericHandlers())
				nums.add(new INumeric(handle, agent));

		}

		@Override
		public void getScanner(IAgent agent, List<IScanner> scanners, IError error)
				throws RemoteException {

			Agent a = checkParameters(agent, scanners, error);
			if (a == null)
				return;

			for (Integer handle: a.mdsHandler.getMDS().getScannerHandlers())
				scanners.add(new IScanner(handle, agent));

		}

		@Override
		public void getRT_SA(IAgent agent, List<IRT_SA> rts, IError error)
				throws RemoteException {

			Agent a = checkParameters(agent, rts, error);
			if (a == null)
				return;

			for (Integer handle: a.mdsHandler.getMDS().getRT_SAHandlers())
				rts.add(new IRT_SA(handle, agent));

		}

		@Override
		public void getEnumeration(IAgent agent, List<IEnumeration> enumeration, IError error)
				throws RemoteException {

			Agent a = checkParameters(agent, enumeration, error);
			if (a == null)
				return;

			for (Integer handle: a.mdsHandler.getMDS().getEnumerationHandlers())
				enumeration.add(new IEnumeration(handle, agent));

		}

		@Override
		public void getPM_Store(IAgent agent, List<IPM_Store> pmStore, IError error)
				throws RemoteException {

			Agent a = checkParameters(agent, pmStore, error);
			if (a == null)
				return;

			for (Integer handle: a.mdsHandler.getMDS().getPM_StoresHandlers())
				pmStore.add(new IPM_Store(handle, agent));

		}

		@Override
		public void getObjectAttrs(IDIMClass obj, List<IAttribute> attrs, IError error)
					throws RemoteException {

			Agent a = checkParameters(obj.getAgent(), attrs, error);
			if (a == null)
				return;

			if (obj == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_OBJECT);
				setErrorMessage(error);
				return;
			}

			DIM o = getObject(a, obj);

			if (o == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_OBJECT);
				setErrorMessage(error);
				return;
			}

			parcelAttributes(o, attrs, error);
		}

		@Override
		public boolean setTime(IAgent agent, IError err)
				throws RemoteException {
			Agent a = getAgent(agent);

			if (err == null) {
				err = new IError();
			}

			if (a == null) {
				err.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(err);
				return false;
			}

			AndroidExternalEvent<Boolean, Object> ev = new AndroidExternalEvent<Boolean, Object>(EventType.REQ_SET_TIME, null);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return false;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);
			return true;
		}

		private <T> Agent checkParameters(IAgent agent, T ret, IError error) {
			if (error == null)
				return null;

			if (ret == null) {
				error.setErrCode(ErrorCodes.INVALID_ARGUMENTS);
				setErrorMessage(error);
				return null;
			}

			if (agent == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(error);
				return null;
			}

			Agent a = getAgent(agent);

			if (a == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(error);
			}

			return a;
		}
	};

	/**
	 * The IAgentService is defined through IDL
	 */
	private final IScannerService.Stub scannerServiceStub = new IScannerService.Stub() {

		@Override
		public boolean setScannerOperationalState(IScanner scanner,
				IOperationalState opState, IError err) throws RemoteException {

			Agent agent = getAgent(scanner.getAgent());
			if (agent == null) {
				err.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(err);
				return false;
			}

			if (opState == null) {
				err.setErrCode(ErrorCodes.INVALID_ATTRIBUTE);
				setErrorMessage(err);
				return false;
			}

			Attribute attr = null;
			OperationalState os = new OperationalState();
			os.setValue(opState.getState());
			try {
				attr = new Attribute(Nomenclature.MDC_ATTR_OP_STAT, os);
			} catch (InvalidAttributeException e) {
				e.printStackTrace();
				return false;
			}

			// Get the handle necessary to create the SetEventData:
			HANDLE handle = new HANDLE();
			INT_U16 value = new INT_U16();
			value.setValue(scanner.getHandle());
			handle.setValue(value);

			SetEventData setEventData = new SetEventData(handle, attr);
			AndroidExternalEvent<Boolean, SetEventData> ev =
				new AndroidExternalEvent<Boolean, SetEventData>(EventType.REQ_SET, setEventData);

			agent.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return false;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);

			return ev.getRspData();
		}

	};

	/**
	 * The IPMStoreService is defined through IDL
	 */
	private final IPMStoreService.Stub pmStoreServiceStub = new IPMStoreService.Stub() {

		@Override
		public boolean updatePMStore(IPM_Store store, IError err) {
			if (err == null) {
				err = new IError();
			}

			if (store == null) {
				err.setErrCode(ErrorCodes.UNKNOWN_PMSTORE);
				setErrorMessage(err);
				return false;
			}

			Agent a;
			IAgent ia = store.getAgent();
			if ( ia == null || (a = getAgent(ia)) == null ) {
				err.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(err);
				return false;
			}

			HANDLE handle = new HANDLE();
			handle.setValue(new INT_U16(store.getHandle()));
			GetPmStoreEventData pmEvent = new GetPmStoreEventData(handle);
			AndroidExternalEvent<Boolean, GetPmStoreEventData> ev = new AndroidExternalEvent<Boolean, GetPmStoreEventData>(EventType.REQ_GET_PM_STORE, pmEvent);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return false;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);

			return ev.getRspData();
		}

		@Override
		public void getAllPMSegments(IPM_Store store, List<IPM_Segment> segments, IError err) throws RemoteException {
			Agent a = checkParameters(store, segments, err);
			if (a == null)
				return;

			HANDLE handle = new HANDLE();
			handle.setValue(new INT_U16(store.getHandle()));

			SegmSelection ss = new SegmSelection();
			ss.selectAll_segments(new INT_U16(new Integer(0)));

			GetPmSegmentEventData event = new GetPmSegmentEventData(handle, ss);
			AndroidExternalEvent<List<PM_Segment>, GetPmSegmentEventData> ev = new AndroidExternalEvent<List<PM_Segment>, GetPmSegmentEventData>(EventType.REQ_GET_SEGMENT_INFO, event);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);

			for (PM_Segment seg: ev.getRspData().toArray(new PM_Segment[0])) {
				if (seg == null || seg.getAttribute(Nomenclature.MDC_ATTR_ID_INSTNO) == null)
					continue;
				InstNumber id = (InstNumber) seg.getAttribute(Nomenclature.MDC_ATTR_ID_INSTNO).getAttributeType();
				if (id != null)
					segments.add(new IPM_Segment(id.getValue(), store));
			}
		}

		private <T> Agent checkParameters(IPM_Store store, T ret, IError error) {
			if (error == null)
				return null;

			if (ret == null) {
				error.setErrCode(ErrorCodes.INVALID_ARGUMENTS);
				setErrorMessage(error);
				return null;
			}

			if (store == null) {
				error.setErrCode(ErrorCodes.UNKNOWN_PMSTORE);
				setErrorMessage(error);
				return null;
			}

			Agent a;
			IAgent ia = store.getAgent();
			if ( ia == null || (a = getAgent(ia)) == null ) {
				error.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(error);
				return null;
			}

			return a;
		}

		@Override
		public boolean startPMSegmentTransfer(IPM_Segment segment, IError err)
				throws RemoteException {
			if (err == null) {
				err = new IError();
			}

			if (segment == null) {
				err.setErrCode(ErrorCodes.UNKNOWN_OBJECT);
				setErrorMessage(err);
				return false;
			}

			Agent a;
			IAgent ia = segment.getAgent();
			if ( ia == null || (a = getAgent(ia)) == null ) {
				err.setErrCode(ErrorCodes.UNKNOWN_AGENT);
				setErrorMessage(err);
				return false;
			}

			HANDLE pm_handle = new HANDLE();
			pm_handle.setValue(new INT_U16(segment.getHandle()));
			InstNumber insNumber = null;

			PM_Store pmStore = a.mdsHandler.getMDS().getPM_Store(pm_handle);
			for (PM_Segment segm: pmStore.getSegments().toArray(new PM_Segment[0])) {
				insNumber = (InstNumber) segm.getAttribute(
						Nomenclature.MDC_ATTR_ID_INSTNO).getAttributeType();
				if (insNumber != null)
					break;
			}

			if (insNumber == null) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			TrigPMSegmentXferEventData eventData = new TrigPMSegmentXferEventData(
					pm_handle, insNumber);
			AndroidExternalEvent<Boolean, TrigPMSegmentXferEventData> ev =
				new AndroidExternalEvent<Boolean, TrigPMSegmentXferEventData>(
						EventType.REQ_TRIG_SEGMENT_DATA_XFER, eventData);
			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				err.setErrCode(ErrorCodes.UNEXPECTED_ERROR);
				setErrorMessage(err);
				return false;
			}

			if (ev.wasError()) {
				err.setErrCode(ev.getError());
				setErrorMessage(err);
				return false;
			}

			err.setErrCode(ErrorCodes.NO_ERROR);
			setErrorMessage(err);

			return ev.getRspData();
		}

		@Override
		public void getPMSegmentsTimeRange(IPM_Store store, long startTime,
				long endTime, List<IPM_Store> stores, IError err)
				throws RemoteException {
			Logging.debug("TODO: implement getPMSegmentsTimeRange");
		}
	};


	@Override
	public IBinder onBind(Intent intent) {
		if (IManagerService.class.getName().equals(intent.getAction()))
			return managerServiceStub;
		if (IAgentService.class.getName().equals(intent.getAction()))
			return agentServiceStub;
		if (IScannerService.class.getName().equals(intent.getAction()))
			return scannerServiceStub;
		if (IPMStoreService.class.getName().equals(intent.getAction()))
			return pmStoreServiceStub;
		return null;
	}

	/**
	 * Sets the error message with the string correct string for the error code already set
	 * @param err The error which error message will be set
	 */
	private void setErrorMessage(IError err) {
		try {
			err.setErrMsg(ErrorFactory.getDefaultErrorGenerator().error2string(err.getErrCode()));
		} catch (ErrorException e) {
			err.setErrMsg(HealthService.this.getString(R.string.UNEXPECTED_ERROR));
		}
	}

}
