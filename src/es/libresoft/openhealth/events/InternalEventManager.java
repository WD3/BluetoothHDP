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

public interface InternalEventManager {
	/**
	 * Agent event to indicate that new measure has been received from agent
	 * @param value
	 * @param date
	 */
	public void receivedMeasure(Agent agent, MeasureReporter mr);

	/**
	 * Agent event to indicate that the agents has changed is state
	 * @param system_id
	 * @param stateCode
	 * @param stateName
	 */
	public void agentChangeState(Agent agent, int stateCode, String stateName);

	/**
	 * Event that indicates that a new agent is available
	 *
	 * @param agent The new agent
	 * */
	public void agentPlugged(Agent agent);

	/**
	 * Event that indicates that an agent is no longer available
	 * @param agent The agent that is being destroyed
	 */
	public void agentUnplugged(Agent agent);

	/**
	 * Event that indicates that an asyncronous error has happen
	 * @param errorCode The error numeric code
	 */
	public void error(Agent agent, int errorCode);
}
