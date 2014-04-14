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

import Config.BloodPressureAgent;
import Config.ManagerConfig;
import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AarqApdu;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.AssociateResult;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfigObjectList;
import ieee_11073.part_20601.asn1.ConfigReport;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.PrstApdu;
import ieee_11073.part_20601.fsm.Associating;
import ieee_11073.part_20601.fsm.Configuring;
import ieee_11073.part_20601.fsm.StateHandler;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.Specialization;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;

public final class SendingConfig extends Configuring {

	public SendingConfig(StateHandler handler) {
		super(handler);
	}
	@Override
	public synchronized String getStateName() {
		return "Sending Config";
	}
	@Override
	public synchronized void process(ApduType apdu) {
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
	public void sendNotiConfig(){
		Logging.debug("sending config");
		ApduType configApdu = ConfigApdu(state_handler.getMDS().getDeviceConf());
		state_handler.send(configApdu);
		state_handler.changeState(new WaitingApproval(state_handler));
	}
	//----------------------------------PRIVATE--------------------------------------------------------
	private ApduType ConfigApdu(DeviceConfig dev_conf) {
		Agent a = (Agent)state_handler.getMDS().getDevice();
		Specialization spec = a.getSpecialization();
		ConfigReport config = new ConfigReport();
		config.setConfig_report_id(new ConfigId(dev_conf.getPhdId()));
		config.setConfig_obj_list(new ConfigObjectList(spec.getConfigdata()));
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(0));
		OID_Type eventtype = new OID_Type();
		eventtype.setValue(new INT_U16(Nomenclature.MDC_NOTI_CONFIG));
		DataApdu data = MessageFactory.PrstRoivCmpConfirmedEventReport(state_handler.getMDS(), config, handle,eventtype);
		return MessageFactory.composeApdu(data, dev_conf);
	}
	@Override
	public int getStateCode() {
		return CONNECTED_ASSOCIATED_CONFIGURING_SENDING_CONFIG;
	}
	

}
