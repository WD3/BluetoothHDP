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

import ieee_11073.part_20601.fsm.StateHandler;

import java.util.TimerTask;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.utils.ASN1_Values;

	/**************************************************************************************************************
	 * 8.4.3 Timeout variables:
	 * There are a few places in the personal health device protocol where timeouts are used. There are both retry
	 * timeout periods and retry counts. The mapping of the timeouts to numerical values is in Timeouts interface
	 **************************************************************************************************************/
public abstract class TimeOut extends TimerTask {
	/* Associating procedure 							Value						Communication			Subclause */
	public static final int TO_ASSOC		=	10000;	/* 10sg (and RC_Assoc=3)	Association				8.7.5	  */
	public static final int TO_CONFIG		=	10000;	/* 10sg						Configuration			8.8.5	  */
	public static final int TO_RELEASE		=	3000;	/* 3sg						Assoc. Release			8.10.5	  */
	/* Operating procedure																						  	  */
	public static final int MDS_TO_CA		=	3000;	/* 3sg						Confirm Action			8.9.5.2	  */
	public static final int MDS_TO_GET		=	3000;	/* 3sg						Get						8.9.5.4	  */
	public static final int MDS_TO_CS		=	3000;	/* 3sg						Confirm Set				8.9.5.5	  */
	public static final int MDS_TO_SP_MDS	=	3000;	/* 3sg						inter-service-timeout	8.9.5.6	  */
	public static final int PM_STORE_TO_CA	=	3000;	/* 3sg						Confirm Action			8.9.5.2	  */
	public static final int PM_STORE_TO_GET	=	3000;	/* 3sg						Get						8.9.5.4	  */
	public static final int PM_STORE_TO_CS	=	3000;	/* 3sg						Confirm Set				8.9.5.5	  */
	public static final int SCANNER_TO_CS	=	3000;	/* 3sg						Confirm Set				8.9.5.5	  */

	public static final int MDS_TO_CER		=	3003;	/* 3sg						Confirm Event Report	8.9.5.3	  */
	public static final int PM_STORE_TO_CER	=	3000;	/* 3sg						Confirm Event Report	8.9.5.3	  */
	public static final int SCANNER_TO_CER	=	3000;	/* 3sg						Confirm Event Report	8.9.5.3	  */

	public static final int RC_ASSOC		=	3;

	private int timeout;
	protected StateHandler stateHandler;

	public TimeOut(int timeout, StateHandler stateHandler) {
		this.timeout = timeout;
		this.stateHandler = stateHandler;
	}

	public final void run() {
			expiredTimeout();
			stateHandler.removeTimeout(this);
	}

	public int getTimeout() {
		return timeout;
	}

	public void start() {
		stateHandler.addTimeout(this);
	}

	/**
	 * This method will be called when the timeout expires
	 * */
	protected void expiredTimeout() {
		Logging.error("Timeout expired.");
		Event event = new Event(EventType.IND_TIMEOUT);
		event.setReason(ASN1_Values.ABRT_RE_RESPONSE_TIMEOUT);
		stateHandler.sendEvent(event);
	}
}
