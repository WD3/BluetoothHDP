/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

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

import org.bn.CoderFactory;
import org.bn.IDecoder;

import ieee_11073.part_10101.Nomenclature;

import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.ConfigReportRsp;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.EventReportResultSimple;
import ieee_11073.part_20601.asn1.PrstApdu;
import ieee_11073.part_20601.asn1.DataApdu.MessageChoiceType;
import ieee_11073.part_20601.fsm.Configuring;
import ieee_11073.part_20601.fsm.StateHandler;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;

public final class WaitingApproval extends Configuring {

	public WaitingApproval(StateHandler handler) {
		super(handler);
	}
	@Override
	public synchronized String getStateName() {
		return "Waiting Approval";
	}
	@Override
	public synchronized void process(ApduType apdu) {
		if(apdu.isPrstSelected()){
			process_PrstApdu(apdu.getPrst());
		}
	}

	@Override
	public synchronized boolean processEvent(Event event) {
		if (event.getTypeOfEvent() == EventType.IND_TRANS_DESC) {
			System.err.println("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
			return true;
		}
		return false;
	}
	//----------------------------------PRIVATE--------------------------------------------------------
	private void process_PrstApdu(PrstApdu prst){
		try {
			processDataApdu(ASN1_Tools.decodeData(prst.getValue(),
					DataApdu.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules()));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error getting DataApdu encoded with " +
					this.state_handler.getMDS().getDeviceConf().getEncondigRules() +
					". The connection will be released.");
			state_handler.send(MessageFactory.RlrqApdu_NORMAL());
			state_handler.changeState(new MDisassociating(state_handler));
		}
	}
	
	private void processDataApdu(DataApdu data) {
		MessageChoiceType msg = data.getMessage();
		//Process the message received
		if (msg.isRors_cmip_confirmed_event_reportSelected()) {
			rors_cmip_confirmed_event_repor(data);
		}
	}
	private void rors_cmip_confirmed_event_repor(DataApdu data){
		//(A.10.3 EVENT REPORT service)
		EventReportResultSimple event = data.getMessage().getRors_cmip_confirmed_event_report();
		if (event.getObj_handle().getValue().getValue() == 0){
			if(event.getEvent_type().getValue().getValue()==Nomenclature.MDC_NOTI_CONFIG){				
				try {
					byte[] raw = event.getEvent_reply_info();
					IDecoder decoderConfRsp = CoderFactory.getInstance().newDecoder(state_handler.getMDS().getDeviceConf().getEncondigRules());
					ConfigReportRsp response = decoderConfRsp.decode(new ByteArrayInputStream(raw),ConfigReportRsp.class);
					switch(response.getConfig_result().getValue()){
					case ASN1_Values.CONF_RESULT_ACCEPTED_CONFIG:
						state_handler.changeState(new MOperating(state_handler));
						break;
					case ASN1_Values.CONF_RESULT_UNSUPPORTED_CONFIG:
						//TODO:
						System.err.println("Warning:Agent CONFIG UNSUPPORTED");
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			System.err.println("Warning: Received Handle=" + event.getObj_handle().getValue().getValue() + " in CheckingConfig state. Ignore.");
		}
	}
	@Override
	public int getStateCode() {
		return CONNECTED_ASSOCIATED_CONFIGURING_WAITING_APPROVAL;
	}

}
