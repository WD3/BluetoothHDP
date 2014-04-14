/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>
Author: Jorge Fernández González <jfernandez@libresoft.es>

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bn.CoderFactory;
import org.bn.IDecoder;
import org.bn.IEncoder;

import Config.BloodPressureAgent;
import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.ConfigObjectList;
import ieee_11073.part_20601.asn1.ConfigReport;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.DataReqId;
import ieee_11073.part_20601.asn1.DataApdu.MessageChoiceType;
import ieee_11073.part_20601.asn1.EventReportArgumentSimple;
import ieee_11073.part_20601.asn1.EventReportResultSimple;
import ieee_11073.part_20601.asn1.GetResultSimple;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.InvokeIDType;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.ObservationScanFixed;
import ieee_11073.part_20601.asn1.PersonId;
import ieee_11073.part_20601.asn1.PrstApdu;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.ScanReportInfoFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoMPFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoMPVar;
import ieee_11073.part_20601.asn1.ScanReportInfoVar;
import ieee_11073.part_20601.asn1.ScanReportPerFixed;
import ieee_11073.part_20601.asn1.SegmentDataEvent;
import ieee_11073.part_20601.asn1.SegmentDataResult;
import ieee_11073.part_20601.asn1.TrigSegmDataXferReq;
import ieee_11073.part_20601.fsm.Operating;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.phd.dim.DIM;
import ieee_11073.part_20601.phd.dim.DimTimeOut;
import ieee_11073.part_20601.phd.dim.EpiCfgScanner;
import ieee_11073.part_20601.phd.dim.PM_Segment;
import ieee_11073.part_20601.phd.dim.PM_Store;
import ieee_11073.part_20601.phd.dim.PeriCfgScanner;
import ieee_11073.part_20601.phd.dim.SET_Service;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.Specialization;

import java.util.List;

import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventAPDUOverflow;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.events.application.GetPmSegmentEventData;
import es.libresoft.openhealth.events.application.GetPmStoreEventData;
import es.libresoft.openhealth.events.application.SetEventData;
import es.libresoft.openhealth.events.application.TrigPMSegmentXferEventData;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;
import es.libresoft.openhealth.utils.RawDataExtractor;

public final class MOperating extends Operating {

	// Next interface is used to process the received PrstApdu depending of
	// the device configuration is 20601 or external

	private interface ProcessHandler {
		public void processPrstApdu(PrstApdu prst);
	};

	private ProcessHandler process;

	public MOperating(StateHandler handler) {
		super(handler);
		int data_proto_id = state_handler.getMDS().getDeviceConf().getDataProtoId();
		if (data_proto_id== ASN1_Values.DATA_PROTO_ID_20601){
			process = new ProcessHandler(){
				@Override
				public void processPrstApdu(PrstApdu prst) {
					process_20601_PrstApdu(prst);
				}
			};
		}else{
			//TODO: Add here support for data-proto-id-external...
			Logging.error("Data_Proto_id: " + data_proto_id +" is not supported");
		}
	}

	@Override
	public synchronized void process(ApduType apdu) {
		if (apdu.isPrstSelected()){
			process.processPrstApdu(apdu.getPrst());
		}else if (apdu.isRlrqSelected()) {
			state_handler.send(MessageFactory.RlreApdu_NORMAL());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if(apdu.isAarqSelected() || apdu.isAareSelected() || apdu.isRlreSelected()){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if(apdu.isAbrtSelected()){
			state_handler.changeState(new MUnassociated(state_handler));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean processEvent(Event event) {
		switch (event.getTypeOfEvent()) {
		case EventType.REQ_GET_PM_STORE:
			ExternalEvent<Boolean, GetPmStoreEventData> pmEvent = (ExternalEvent<Boolean, GetPmStoreEventData>) event;
			PM_Store pm_store = this.state_handler.getMDS().getPM_Store(pmEvent.getPrivData().getHandle());
			pm_store.GET(pmEvent);
			return true;

		case EventType.REQ_SET:
			ExternalEvent<Boolean, SetEventData> setEvent = (ExternalEvent<Boolean, SetEventData>) event;
			DIM obj = state_handler.getMDS().getObject(setEvent.getPrivData().getObjectHandle());
			try {
				SET_Service serv = (SET_Service) obj;
				serv.SET(setEvent, setEvent.getPrivData().getAttribute());
			} catch (ClassCastException e) {
				Logging.error("Set cannot be done in object: " + setEvent.getPrivData().getObjectHandle().getValue().getValue() +
							" it does not implement a SET service");
			}
			return true;

		case EventType.REQ_MDS:
			state_handler.getMDS().GET(event);
			return true;

		case EventType.REQ_GET_SEGMENT_INFO:
			ExternalEvent<List<PM_Segment>, GetPmSegmentEventData> pmSegEvent = (ExternalEvent<List<PM_Segment>, GetPmSegmentEventData>) event;
			PM_Store store = this.state_handler.getMDS().getPM_Store(pmSegEvent.getPrivData().getHandle());
			store.Get_Segment_Info(pmSegEvent, pmSegEvent.getPrivData().getSegmSelection());
			return true;

		case EventType.REQ_TRIG_SEGMENT_DATA_XFER:
			ExternalEvent<Boolean, TrigPMSegmentXferEventData> xferEvent = (ExternalEvent<Boolean, TrigPMSegmentXferEventData>) event;
			PM_Store s = this.state_handler.getMDS().getPM_Store(xferEvent.getPrivData().getStoreHandle());

			TrigSegmDataXferReq tsdxr = new TrigSegmDataXferReq();
			tsdxr.setSeg_inst_no(xferEvent.getPrivData().getInsNumber());
			s.Trig_Segment_Data_Xfer(xferEvent, tsdxr);
			return true;

		case EventType.REQ_SET_TIME:
			state_handler.getMDS().Set_Time(event);
			return true;

		case EventType.IND_TRANS_DESC:
			Logging.error("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
			return true;

		case EventType.IND_TIMEOUT:
			state_handler.send(MessageFactory.AbrtApdu(event.getReason()));
			state_handler.changeState(new MUnassociated(state_handler));
			return true;

		case EventType.REQ_ASSOC_REL:
			state_handler.send(MessageFactory.RlrqApdu_NORMAL());
			state_handler.changeState(new MDisassociating(state_handler));
			try {
				ExternalEvent<Boolean, Object> eevent = (ExternalEvent<Boolean, Object> ) event;
				eevent.processed(true, ErrorCodes.NO_ERROR);
			} catch (ClassCastException e) {

			}
			return true;

		case EventType.REQ_ASSOC_ABORT:
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
			return true;

		case EventType.REC_CORRUPTED_APDU:
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
			return true;

		case EventType.REC_APDU_OVERFLOW:
			EventAPDUOverflow eao = (EventAPDUOverflow)event;

			if (!eao.getApduType().isPrstSelected()) {
				Logging.error("APDU exceeded maximum length is non PrstApdu");
				state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
				state_handler.changeState(new MUnassociated(state_handler));
			}

			try {
				DataApdu data = ASN1_Tools.decodeData(eao.getApduType().getPrst().getValue(),
						DataApdu.class,
						this.state_handler.getMDS().getDeviceConf().getEncondigRules());
				state_handler.send(MessageFactory.ROER_PROTOCOL_VIOLATION_Apdu(data, state_handler.getMDS().getDeviceConf()));
			} catch (Exception e) {
				e.printStackTrace();
				state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
				state_handler.changeState(new MUnassociated(state_handler));
			}

			return true;
		case EventType.NOTI_MEASURE:
			System.out.println("sending measure");
			state_handler.send(generateMeasureApdu());
			return true;

		default:
			return false;
		}

	}

	//----------------------------------PRIVATE--------------------------------------------------------

	private void process_20601_PrstApdu(PrstApdu prst){
		try {
			/*
			 * The DataApdu and the related structures in A.10 shall use encoding rules
			 * as negotiated during the association procedure as described in 8.7.3.1.
			 */
			processDataApdu(ASN1_Tools.decodeData(prst.getValue(),
					DataApdu.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules()));
		} catch (Exception e) {
			e.printStackTrace();
			Logging.error("Error getting DataApdu encoded with " +
					this.state_handler.getMDS().getDeviceConf().getEncondigRules() +
					". The connection will be released.");
			state_handler.send(MessageFactory.RlrqApdu_NORMAL());
			state_handler.changeState(new MDisassociating(state_handler));
		}
	}

	private void processDataApdu(DataApdu data) {
		MessageChoiceType msg = data.getMessage();
		//Process the message received
		if (msg.isRoiv_cmip_event_reportSelected()) {
			Logging.debug(">> Roiv_cmip_event_report");
			roiv_cmip_event_report(data);
		}else if (msg.isRoiv_cmip_confirmed_event_reportSelected()) {
			Logging.debug(">> Roiv_cmip_confirmed_event_report");
			roiv_cmip_confirmed_event_report(data);
		}else if (msg.isRoiv_cmip_getSelected()) {
			//TODO:
			Logging.debug(">> Roiv_cmip_get");
			roiv_cmip_get(data);
		}else if (msg.isRoiv_cmip_setSelected()) {
			//TODO:
			Logging.debug(">> Roiv_cmip_set");
		}else if (msg.isRoiv_cmip_confirmed_setSelected()) {
			//TODO:
			Logging.debug(">> Roiv_cmip_confirmed_set");
		}else if (msg.isRoiv_cmip_actionSelected()){
			//TODO:
			Logging.debug(">> Roiv_cmip_action");
		}else if (msg.isRoiv_cmip_confirmed_actionSelected()) {
			//TODO:
			Logging.debug(">> Roiv_cmip_confirmed_action");
		}else if (msg.isRors_cmip_confirmed_event_reportSelected()){
			//TODO:
			Logging.debug(">> Rors_cmip_confirmed_event_report");
		}else if (msg.isRors_cmip_getSelected()){
			//TODO:
			Logging.debug(">> Rors_cmip_get");
		}else if (msg.isRors_cmip_confirmed_setSelected()){
			//TODO:
			Logging.debug(">> Rors_cmip_confirmed_set");
		}else if (msg.isRors_cmip_confirmed_actionSelected()){
			//TODO:
			Logging.debug(">> Rors_cmip_confirmed_action");
		}else if (msg.isRoerSelected()){
			//TODO:
			Logging.debug(">> Roer");
		}else if (msg.isRorjSelected()){
			//TODO:
			Logging.debug(">> Rorj");
		}

		DimTimeOut to = state_handler.retireTimeout(data.getInvoke_id().getValue());
		if (to != null)
			to.procResponse(data);
	}

	private void processSegmentDataEvent(InvokeIDType id, EventReportArgumentSimple event) {
		PM_Store pmstore = this.state_handler.getMDS().getPM_Store(event.getObj_handle());
		if (pmstore == null)
			return;

		RelativeTime rt = event.getEvent_time();
		Logging.debug("Relative Time: " + rt.getValue().getValue().intValue());

		try {
			SegmentDataEvent sde = ASN1_Tools.decodeData(event.getEvent_info(),
									SegmentDataEvent.class,
									this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			SegmentDataResult sdr = pmstore.Segment_Data_Event(sde);

			EventReportResultSimple errs = MessageFactory.genEventReportResultSimple(event, sdr,
											state_handler.getMDS().getDeviceConf().getEncondigRules());
			DataApdu data= new DataApdu();
			MessageChoiceType mct = new MessageChoiceType();
			mct.selectRors_cmip_confirmed_event_report(errs);
			data.setInvoke_id(id);
			data.setMessage(mct);

			state_handler.send(MessageFactory.composeApdu(data, state_handler.getMDS().getDeviceConf()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processBufScannerEvent(EventReportArgumentSimple event) {
		try {
			PeriCfgScanner scanner = (PeriCfgScanner) state_handler.getMDS().getScanner(event.getObj_handle());
			switch (event.getEvent_type().getValue().getValue()) {
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_VAR:
				scanner.Buf_Scan_Report_Var(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_FIXED:
				scanner.Buf_Scan_Report_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_GROUPED:
				scanner.Buf_Scan_Report_Grouped(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_VAR:
				scanner.Buf_Scan_Report_MP_Var(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED:
				scanner.Buf_Scan_Report_MP_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED:
				scanner.Buf_Scan_Report_MP_Grouped(event);
				break;
			}
		} catch(ClassCastException e) {
			Logging.error("Only Periodic Scanners can receive Buffered scanner events");
		}
	}

	private void processUnbufScannerEvent(EventReportArgumentSimple event) {
		try {
			EpiCfgScanner scanner = (EpiCfgScanner) state_handler.getMDS().getScanner(event.getObj_handle());
			switch (event.getEvent_type().getValue().getValue()) {
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_VAR:
				scanner.Unbuf_Scan_Report_Var(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_FIXED:
				scanner.Unbuf_Scan_Report_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED:
				scanner.Unbuf_Scan_Report_Grouped(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR:
				scanner.Unbuf_Scan_Report_MP_Var(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED:
				scanner.Unbuf_Scan_Report_MP_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED:
				scanner.Unbuf_Scan_Report_MP_Grouped(event);
				break;
			}
		} catch(ClassCastException e) {
			Logging.error("Only Episodic Scanner can receive UnBuffered scanner events");
		}
	}

	private void roiv_cmip_confirmed_event_report(DataApdu data){
		EventReportArgumentSimple event = data.getMessage().getRoiv_cmip_confirmed_event_report();

		if (event.getEvent_type().getValue().getValue().intValue() == Nomenclature.MDC_NOTI_SEGMENT_DATA) {
			// PM-store object events (only confirmed):
			processSegmentDataEvent(data.getInvoke_id(), event);
		}else {
			process_event_report(data, event);
		}
	}

	private void roiv_cmip_event_report(DataApdu data){
		EventReportArgumentSimple event = data.getMessage().getRoiv_cmip_event_report();
		process_event_report(data, event);
	}

	private void process_event_report(DataApdu data, EventReportArgumentSimple event){
		//(A.10.3 EVENT REPORT service)
		if (event.getObj_handle().getValue().getValue().intValue() == 0){
			//obj-handle is 0 to represent the MDS
			process_MDS_Object_Event(event);
			this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS()));
		} else {
			switch (event.getEvent_type().getValue().getValue().intValue()) {
			// Episodic configurable scanner object events:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_VAR:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_FIXED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED:
				processUnbufScannerEvent(event);
				this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS()));
				break;
			// Periodic configurable scanner object events:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_VAR:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_FIXED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_GROUPED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_VAR:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED:
				processBufScannerEvent(event);
				this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS()));
				break;
			default:
				//TODO: handle representing a scanner or PM-store object.
				Logging.error("Warning: Received Handle=" + event.getObj_handle().getValue().getValue() + " in Operating state. Ignore.");
			}
		}
	}

	private void process_MDS_Object_Event(EventReportArgumentSimple event){
		switch (event.getEvent_type().getValue().getValue().intValue()){
			case Nomenclature.MDC_NOTI_CONFIG:
				//TODO:
				Logging.debug("MDC_NOTI_CONFIG");
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_VAR:
				Logging.debug("MDC_NOTI_SCAN_REPORT_VAR");
				mdc_noti_scan_report_var(event.getEvent_info());
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_FIXED:
				Logging.debug("MDC_NOTI_CONFIG");
				mdc_noti_scan_report_fixed(event.getEvent_info());
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_VAR:
				Logging.debug("MDC_NOTI_SCAN_REPORT_MP_VAR");
				mdc_noti_scan_report_MP_var(event.getEvent_info());
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_FIXED:
				Logging.debug("MDC_NOTI_SCAN_REPORT_MP_FIXED");
				mdc_noti_scan_report_MP_fixed(event.getEvent_info());
				break;
		}
	}

	private void mdc_noti_scan_report_fixed (byte[] einfo){
		try {
			ScanReportInfoFixed srif = ASN1_Tools.decodeData(einfo,
					ScanReportInfoFixed.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_Fixed(srif);
		} catch (Exception e) {
			Logging.debug("Error decoding mdc_noti_scan_report_fixed");
			e.printStackTrace();
		}

	}

	private void mdc_noti_scan_report_var(byte[] einfo) {
		try {
			ScanReportInfoVar sriv = ASN1_Tools.decodeData(einfo,
					ScanReportInfoVar.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_Var(sriv);
		} catch (Exception e) {
			Logging.debug("Error decoding mdc_noti_scan_report_var");
			e.printStackTrace();
		}

	}

	private void mdc_noti_scan_report_MP_fixed (byte[] einfo){
		try {
			ScanReportInfoMPFixed srimpf = ASN1_Tools.decodeData(einfo,
					ScanReportInfoMPFixed.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_MP_Fixed(srimpf);
		} catch (Exception e) {
			Logging.debug("Error decoding mdc_noti_scan_report_fixed");
			e.printStackTrace();
		}

	}

	private void mdc_noti_scan_report_MP_var(byte[] einfo) {
		try {
			ScanReportInfoMPVar srimpv = ASN1_Tools.decodeData(einfo,
					ScanReportInfoMPVar.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_MP_Var(srimpv);
		} catch (Exception e) {
			Logging.debug("Error decoding mdc_noti_scan_report_var");
			e.printStackTrace();
		}

	}

	
	private ApduType generateMeasureApdu() {
		Agent a = (Agent)state_handler.getMDS().getDevice();
		Specialization spec = a.getSpecialization();
		Collection<Map<HANDLE, Object>> measures = spec.getMeasures();
		ScanReportInfoMPFixed info = generateScanReportInfo(measures);
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(0));
		OID_Type eventtype = new OID_Type();
		eventtype.setValue(new INT_U16(Nomenclature.MDC_NOTI_SCAN_REPORT_MP_FIXED));
		DataApdu data = MessageFactory.PrstRoivCmpConfirmedEventReport(state_handler.getMDS(), info, handle,eventtype);
		return MessageFactory.composeApdu(data, state_handler.getMDS().getDeviceConf());
	}
	private ScanReportPerFixed generateScanReportPerFixed(Map<HANDLE, Object> measures){
		ScanReportPerFixed srpf = new ScanReportPerFixed();
		List<ObservationScanFixed> obslist = new ArrayList<ObservationScanFixed>();
		
		Set<HANDLE> handles = measures.keySet();
		Iterator<HANDLE> it = handles.iterator();
		ByteArrayOutputStream output = new ByteArrayOutputStream();	
		while (it.hasNext()){
			HANDLE handle = it.next();
			ArrayList<Object> measure = (ArrayList<Object>) measures.get(handle);
			Iterator<Object> eleit = measure.iterator();
			while(eleit.hasNext())
			{
				Object ele = eleit.next();
				try {
					IEncoder<Object> encoder = CoderFactory.getInstance().newEncoder("MDER");
					encoder.encode(ele, output);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ObservationScanFixed obs = new ObservationScanFixed();
			obs.setObj_handle(handle);
			obs.setObs_val_data(output.toByteArray());	
			obslist.add(obs);
		}
		srpf.setObs_scan_fixed(obslist);
		srpf.setPerson_id(new PersonId(1));
		return srpf;
	}
	private <T> ScanReportInfoMPFixed generateScanReportInfo(Collection<Map<HANDLE,Object>> measures){
		ScanReportInfoMPFixed info = new ScanReportInfoMPFixed();
		List<ScanReportPerFixed> srpflist = new ArrayList<ScanReportPerFixed>();
		
		Iterator<Map<HANDLE, Object>> it = measures.iterator();
		while (it.hasNext()){
			Map<HANDLE, Object> measure = it.next();
			HANDLE handle = new HANDLE();
			handle.setValue(new INT_U16(1));
			ScanReportPerFixed srpf = generateScanReportPerFixed(measure);
			srpflist.add(srpf);
		}	
		info.setScan_per_fixed(srpflist);
		info.setScan_report_no(0);
		info.setData_req_id(new DataReqId(0));
		return info;
	}
	private void roiv_cmip_get(DataApdu data){
		state_handler.send(MessageFactory.PrstTypeResponse(data,state_handler.getMDS()));
	}
	
}
