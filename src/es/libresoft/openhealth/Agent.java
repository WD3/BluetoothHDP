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

package es.libresoft.openhealth;

import java.util.Iterator;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.utils.DIM_Tools;
import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.fsm.manager.ManagerStateController;
import ieee_11073.part_20601.phd.channel.InitializedException;
import ieee_11073.part_20601.phd.dim.IMDS_Handler;
import ieee_11073.part_20601.phd.dim.MDS;
import ieee_11073.part_20601.phd.dim.manager.MDSManager;


public final class Agent extends Device {

	private ManagerStateController stc;
	private MDS mdsObj;
	private Specialization spec;
	
	public final IMDS_Handler mdsHandler = new IMDS_Handler(){
		@Override
		public synchronized MDS getMDS() {
			return mdsObj;
		}

		@Override
		public synchronized void setMDS(MDS mds) {
			if (mds == null)
				return;

			mdsObj = mds;
			mdsObj.setDevice(Agent.this);
		}
	};


	public Agent(String transportDescription) {
		super(transportDescription);
		mdsObj = new MDSManager(this);
		stc = new ManagerStateController (mdsHandler);
		stc.configureController(this.inputQueue, this.outputQueue, this.eventQueue);

		try {
			stc.initFSMController();
		} catch (InitializedException e) {
			e.printStackTrace();
		}
	}

	public void setSpecialization(Specialization spec)
	{
		this.spec = spec;
		stc.generateMDS(spec);
	}

	public Specialization getSpecialization()
	{
		return spec;
	}
	
	@Override
	public void freeResources() {
		super.freeResources();
		stc.freeResources();
	}

	public void sendEvent(Event event){
		stc.processEvent(event);
	}

	public Iterator<Integer> getPM_StoresHandlers() {
		return mdsObj.getPM_StoresHandlers().iterator();
	}

	public Iterator<Integer> getScannerHandlers() {
		return mdsObj.getScannerHandlers().iterator();
	}

	@Override
	public void notifyDevicePlugged() {
		InternalEventReporter.agentPlugged(this);
	}

	@Override
	public void notifyDeviceUnplugged() {
		InternalEventReporter.agentUnplugged(this);
	}

	public int getStateCode() {
		return stc.getStateCode();
	}

	public String getStateName() {
		return stc.getStateName();
	}
}
