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

package ieee_11073.part_20601.phd.dim;

import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.fsm.StateHandler;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.logging.Logging;

public abstract class DimTimeOut extends TimeOut {

	private int invokeId;
	private Event event = null;

	public DimTimeOut(int timeout, int invokeId, StateHandler stateHandler) {
		super(timeout, stateHandler);

		this.invokeId = invokeId;
	}

	public int getInvokeId() {
		return invokeId;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	private int getProperTimeoutError() {
		switch (event.getTypeOfEvent()) {
		case EventType.REQ_SET_TIME:
			return ErrorCodes.TIMEOUT_MDS_CONF_ACION;
		case EventType.REQ_GET_PM_STORE:
			return ErrorCodes.TIMEOUT_PM_GET;
		case EventType.REQ_MDS:
			return ErrorCodes.TIMEOUT_MDS_GET;
		case EventType.REQ_GET_SEGMENT_INFO:
		case EventType.REQ_TRIG_SEGMENT_DATA_XFER:
		default:
			Logging.error("Unknown timeout for external event "
					+ event.getTypeOfEvent());
			return ErrorCodes.TIMEOUT;
		}
	}

	/**
	 * External events should unblock the client's call when timeout expires.
	 */
	protected void expiredTimeout() {
		super.expiredTimeout();

		if (event instanceof ExternalEvent<?, ?>)
			((ExternalEvent<?, ?>) event).processed(null,
					getProperTimeoutError());
	}

	/**
	 * This Method will be called by the FSM when the response
	 * is received in order to process the response
	 * */
	public abstract void procResponse(DataApdu data);
}
