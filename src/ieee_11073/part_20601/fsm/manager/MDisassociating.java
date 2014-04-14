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

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventAPDUOverflow;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.fsm.Disassociating;
import ieee_11073.part_20601.fsm.StateHandler;

public final class MDisassociating extends Disassociating {

	public MDisassociating(StateHandler handler) {
		super(handler);
	}

	@Override
	public synchronized void process(ApduType apdu) {
		if(apdu.isAarqSelected() || apdu.isAareSelected()){
			//Should not happen
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if (apdu.isRlrqSelected()) {
			//Both sides releasing connection. Wait for own rlre
			state_handler.send(MessageFactory.RlreApdu_NORMAL());
		}else if(apdu.isRlreSelected()){
			//Release process completed. Exit to unassociated
			state_handler.changeState(new MUnassociated(state_handler));
		}else if(apdu.isAbrtSelected()){
			state_handler.changeState(new MUnassociated(state_handler));
		}//TODO:
		//else rors-*,roer, or rorj -> Abort
	}

	@Override
	public synchronized boolean processEvent(Event event) {
		if (event.getTypeOfEvent() == EventType.REC_CORRUPTED_APDU) {
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
		} else if (event.getTypeOfEvent() == EventType.IND_TRANS_DESC) {
			Logging.error("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
		}else if (event.getTypeOfEvent() == EventType.IND_TIMEOUT) {
			state_handler.send(MessageFactory.AbrtApdu_CONFIGURATION_TIMEOUT());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if (event.getTypeOfEvent() == EventType.REQ_ASSOC_ABORT){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if (event.getTypeOfEvent() == EventType.REC_APDU_OVERFLOW){
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
		}else
			return false;
		return true;
	}

}
