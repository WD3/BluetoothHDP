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

package es.libresoft.openhealth.events;

import es.libresoft.openhealth.Agent;

public class InternalEventReporter {

	private static InternalEventManager iEvent;

	public static synchronized void setDefaultEventManager (InternalEventManager handler){
		iEvent = handler;
	}

	public static void agentPlugged(Agent agent) {
		if (iEvent != null)
			iEvent.agentPlugged(agent);
	}

	public static void agentUnplugged(Agent agent) {
		if (iEvent != null)
			iEvent.agentUnplugged(agent);
	}

	public static void agentChangeStatus(Agent agent, int stateCode, String stateName) {
		if (iEvent!=null)
			iEvent.agentChangeState(agent, stateCode, stateName);
	}

	public static void receivedMeasure(Agent agent, MeasureReporter mr) {
		if (iEvent != null)
			iEvent.receivedMeasure(agent, mr);
	}

	public static void error(Agent agent, int errorCode) {
		iEvent.error(agent, errorCode);
	}
}
