/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot Nemesio <scarot@libresoft.es>

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

package es.libresoft.openhealth.android.aidl;

import es.libresoft.openhealth.android.aidl.IAgent;
import es.libresoft.openhealth.android.aidl.types.IError;
import es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric;
import es.libresoft.openhealth.android.aidl.IState;

oneway interface IManagerClientCallback {
	/**
	 * Called when agent is available for to connect with the manager.
	 */
	void agentPlugged(in IAgent agent);

	/**
	 * Called when agent releases the association with the manager.
	 */
	void agentUnplugged(in IAgent agent);

	/**
	 * Called when the state of the agent changes.
	 */
	void agentChangeState(in IAgent agent, in IState state);

	/**
	 * Notifies asynchronous error in the agent
	 */
	void error(in IAgent agent, in IError error);

	/**
	 * Notifies new meassures that are receive in the agent
	 */
	void agentNewMeassure(in IAgent agent, in IAgentMetric metric);
}