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

import ieee_11073.part_20601.asn1.AarqApdu;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.DataProto;
import ieee_11073.part_20601.asn1.DataProtoList;
import ieee_11073.part_20601.asn1.DataReqModeCapab;
import ieee_11073.part_20601.asn1.PhdAssociationInformation;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.fsm.Unassociated;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.manager.MDSManager;

import java.util.Collection;
import java.util.Iterator;

import Config.AgentConfig;
import Config.ManagerConfig;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.Device;
import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.DeviceConfigCreator;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.ConfigStorageFactory;
import es.libresoft.openhealth.storage.StorageException;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;

public final class MUnassociated extends Unassociated {

	public MUnassociated(StateHandler handler) {
		super(handler);
	}

	@Override
	public synchronized void process(ApduType apdu) {
		if (apdu.isAarqSelected()){
			MAssociating associating = new MAssociating(state_handler);
			associating.associating(apdu.getAarq());
		} else if (apdu.isAareSelected() || apdu.isRlrqSelected() || apdu.isPrstSelected()){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean processEvent(Event event) {
		if (event.getTypeOfEvent() == EventType.REC_CORRUPTED_APDU) {
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			return true;
		} else if (event.getTypeOfEvent() == EventType.IND_TRANS_DESC) {
			Logging.error("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
			try {
				ExternalEvent<Boolean, Object> eevent = (ExternalEvent<Boolean, Object> ) event;
				eevent.processed(true, ErrorCodes.NO_ERROR);
			} catch (ClassCastException e) {

			}
			return true;
		} else if (event.getTypeOfEvent() == EventType.REC_APDU_OVERFLOW){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			return true;
		}else if(event.getTypeOfEvent() == EventType.REQ_ASSOC){
			state_handler.send(MessageFactory.AarqApdu_20601(state_handler.getMDS().getDeviceConf()));
			state_handler.changeState(new MAssociating(state_handler));
			return true;
		}
		else
			return false;

		//else: Ignore
		/*
		}else if (event.getTypeOfEvent() == EventType.REQ_ASSOC_REL){
			Logging.error("2.6) REQ AssocRel. Should not happen. Ignore");
		}else if (event.getTypeOfEvent() == EventType.REQ_ASSOC_ABORT) {
			Logging.error("2.7) REQ AssocAbort. Should not happen. Ignore");
		}else{
			Logging.error("Warning: unexpected event (" + event.getTypeOfEvent() + ") arrived in disconnected state.");
		}
		*/
	}
}
