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
package ieee_11073.part_20601.fsm;

import es.libresoft.openhealth.events.Event;
import ieee_11073.part_20601.asn1.ApduType;

	/**
	 * This is the top level class for all states of the association  state machine defined
	 * in ISO/IEEE 11073-20601.
	 */
public abstract class State {

	public static final int DISCONNECTED										= 1;
	public static final int CONNECTED_UNASSOCIATED								= 2;
	public static final int CONNECTED_ASSOCIATING								= 3;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_SENDING_CONFIG		= 4;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_WAITING_APPROVAL	= 5;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_WAITING			= 6;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_CHECKING_CONFIG	= 7;
	public static final int CONNECTED_ASSOCIATED_OPERATING						= 8;
	public static final int CONNECTED_DISASSOCIATING							= 9;

	protected StateHandler state_handler;

	public State (StateHandler handler) {
		this.state_handler = handler;
	}

	/**
	 * Process an APDU that has arrived on the input data stream
	 * @param apdu
	 */
	public abstract void process(ApduType apdu);

	/**
	 * Process an outer event
	 * @param apdu
	 */
	public abstract boolean processEvent(Event event);

	public abstract String getStateName();

	public abstract int getStateCode();
}
