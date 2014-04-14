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

	/**
	 * This is the MDS class.
	 * The top-level object of each agent is instantiated from the MDS class. Each agent
	 * has one MDS object. The MDS represents the identification and status of the agent
	 * through its attributes.
	 */

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.fsm.StateHandler;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import es.libresoft.openhealth.Device;
import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.ConfigStorageFactory;
import es.libresoft.openhealth.storage.StorageException;

	/**
	 * Each personal health device agent is defined by an object-oriented model. The top-level object
	 * of each agent is instantiated from the MDS class. Each agent has one MDS object. The MDS represents
	 * the identification and status of the agent through its attributes.
	 */

public abstract class MDS extends DIM implements MDS_Events, GET_Service {

	private static int[] mandatoryIds = {Nomenclature.MDC_ATTR_ID_HANDLE,
						  Nomenclature.MDC_ATTR_ID_MODEL,
						  Nomenclature.MDC_ATTR_SYS_ID,
						  Nomenclature.MDC_ATTR_DEV_CONFIG_ID};

	protected Device device;
	private DeviceConfig dev_conf;

	private Hashtable<Integer,Scanner> scanners;
	private Hashtable<Integer,Numeric> numerics;
	private Hashtable<Integer,RT_SA> rt_sas;
	private Hashtable<Integer,Enumeration> enumerations;
	private Hashtable<Integer,PM_Store> pm_stores;

	private int invokeId = 0;
	private StateHandler stateHandler;

	public MDS(Device device) {
		this.device = device;
		clearObjectsFromMds();
	}

	/**
	 * Used only in extended configuration when the agent configuration is unknown
	 */
	public MDS(byte[] system_id, ConfigId devConfig_id){
		this.attributeList = generateMandatoryMDSAttributes(system_id, devConfig_id);
		clearObjectsFromMds();
	}

	public MDS(Hashtable<Integer,Attribute> attributes) throws InvalidAttributeException {
		super(attributes);
		clearObjectsFromMds();
	}

	public boolean setDeviceConfig(DeviceConfig dev_conf){
		if (this.dev_conf == null){
			this.dev_conf = dev_conf;
			return true;
		}else
			return false;
	}

	public void setDevice(Device dev) { this.device = dev;}
	public Device getDevice() { return this.device;}

	public DeviceConfig getDeviceConf(){return this.dev_conf;}

	protected void clearObjectsFromMds () {
		scanners = new Hashtable<Integer,Scanner>();
		numerics = new Hashtable<Integer,Numeric>();
		rt_sas = new Hashtable<Integer,RT_SA>();
		enumerations = new Hashtable<Integer,Enumeration>();
		pm_stores = new Hashtable<Integer,PM_Store>();
	}

	@Override
	protected void checkAttributes(
			Hashtable<Integer, Attribute> attributes)
			throws InvalidAttributeException {
		/* Check mandatory attributes */
		for (int i=0; i<mandatoryIds.length; i++){
			if (!attributes.containsKey(mandatoryIds[i]))
				throw new InvalidAttributeException("Attribute id " + mandatoryIds[i] + " is not assigned.");
		}
	}

	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_VMS_MDS_SIMP;
	}

	public DIM getObject(HANDLE handle) {
		DIM dim;
		int key = handle.getValue().getValue();
		if (key == 0)
			return this;

		dim = scanners.get(key);
		if (dim != null)
			return dim;

		dim = numerics.get(key);
		if (dim != null)
			return dim;

		dim = rt_sas.get(key);
		if (dim != null)
			return dim;

		dim = enumerations.get(key);
		if (dim != null)
			return dim;

		dim = pm_stores.get(key);
		return dim;
	}

	public Scanner getScanner (HANDLE handle){
		return scanners.get(handle.getValue().getValue());
	}

	public void addScanner (Scanner scanner){
		HANDLE handle = (HANDLE)scanner.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
		scanners.put(handle.getValue().getValue(), scanner);
		scanner.setMDS(this);
	}

	public Numeric getNumeric (HANDLE handle){
		return numerics.get(handle.getValue().getValue());
	}

	public void addNumeric (Numeric numeric){
		HANDLE handle = (HANDLE)numeric.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
		numerics.put(handle.getValue().getValue(), numeric);
		numeric.setMDS(this);
	}

	public RT_SA getRT_SA (HANDLE handle){
		return rt_sas.get(handle.getValue().getValue());
	}
	
	public void addRT_SA (RT_SA rt_sa){
		HANDLE handle = (HANDLE)rt_sa.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
		rt_sas.put(handle.getValue().getValue(), rt_sa);
		rt_sa.setMDS(this);
	}
	
	public Enumeration getEnumeration (HANDLE handle){
		return enumerations.get(handle.getValue().getValue());
	}

	public void addEnumeration(Enumeration enumeration) {
		HANDLE handle = (HANDLE)enumeration.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
		enumerations.put(handle.getValue().getValue(), enumeration);
		enumeration.setMDS(this);
	}

	public PM_Store getPM_Store (HANDLE handle){
		return pm_stores.get(handle.getValue().getValue());
	}

	public void addPM_Store (PM_Store pmstore){
		HANDLE handle = (HANDLE)pmstore.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
		pm_stores.put(handle.getValue().getValue(), pmstore);
		pmstore.setMDS(this);
	}

	public synchronized int getNextInvokeId() {
		return invokeId++;
	}

	public void setStateHandler(StateHandler sh) {
		stateHandler = sh;
	}

	public StateHandler getStateHandler() {
		return stateHandler;
	}

	public Set<Integer> getPM_StoresHandlers() {
		return pm_stores.keySet();
	}

	public Set<Integer> getScannerHandlers() {
		return scanners.keySet();
	}

	public Set<Integer> getNumericHandlers() {
		return numerics.keySet();
	}

	public Set<Integer> getRT_SAHandlers() {
		return rt_sas.keySet();
	}

	public Set<Integer> getEnumerationHandlers() {
		return enumerations.keySet();
	}

	/* MDS Object methods */

	/**
	 * This method allows the manager system to enable or disable measurement data transmission
	 * from the agent (see 8.9.3.3.3 for a description).
	 */
	public abstract void MDS_DATA_REQUEST ();

	/**
	 * This method allows the manager system to set a real-time clock (RTC) with the
	 * absolute time. The agent indicates whether the Set-Time command is valid by
	 * using the mds-time-capab-set-clock bit in the Mds-Time-Info attribute.
	 */
	public abstract void Set_Time (Event event);


	//----------------------------------PRIVATE----------------------------------------
	private Hashtable<Integer,Attribute> generateMandatoryMDSAttributes (byte[] system_id, ConfigId devConfig_id){
		Hashtable<Integer,Attribute> mandatoryAttributes = new Hashtable<Integer,Attribute>();
		try {
			//MDS Handle=0
			HANDLE handle = new HANDLE();
			handle.setValue(new INT_U16(new Integer(0)));
			mandatoryAttributes.put(Nomenclature.MDC_ATTR_ID_HANDLE,
					new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
									handle));

			//{"Manufacturer","Model"}
			SystemModel systemModel = new SystemModel();
			systemModel.setManufacturer("Manufacturer".getBytes("ASCII"));
			systemModel.setModel_number("Model".getBytes("ASCII"));
			mandatoryAttributes.put(Nomenclature.MDC_ATTR_ID_MODEL,
					new Attribute(Nomenclature.MDC_ATTR_ID_MODEL,
									systemModel));

			mandatoryAttributes.put(Nomenclature.MDC_ATTR_SYS_ID,
					new Attribute(Nomenclature.MDC_ATTR_SYS_ID,
									system_id));

			mandatoryAttributes.put(Nomenclature.MDC_ATTR_DEV_CONFIG_ID,
					new Attribute(Nomenclature.MDC_ATTR_DEV_CONFIG_ID,
									devConfig_id));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mandatoryAttributes;
	}

	private <T> void storeList(byte[] sys_id, Iterator<T> it) throws StorageException {
		while (it.hasNext()) {
			DIM next;
			try {
				next = (DIM) it.next();
			} catch (ClassCastException e) {
				throw new StorageException(e);
			}
			next.storeConfiguration(sys_id, dev_conf);
		}
	}

	public void storeConfiguration() {
		ConfigStorage cs = null;
		byte[] sys_id = null;

		try {
			sys_id = (byte[]) getAttribute(Nomenclature.MDC_ATTR_SYS_ID).getAttributeType();
			cs = ConfigStorageFactory.getDefaultConfigStorage();

			try {
				storeConfiguration(sys_id, dev_conf);

				storeList(sys_id, scanners.values().iterator());
				storeList(sys_id, numerics.values().iterator());
				storeList(sys_id, rt_sas.values().iterator());
				storeList(sys_id, enumerations.values().iterator());
				storeList(sys_id, pm_stores.values().iterator());

			} catch (StorageException e) {
				Logging.error("Storage aborted, deleting all configurations");
				cs.delete(sys_id, dev_conf);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logging.error("An error ocurred during configuration storage, storage aborted.");
			if (cs != null && sys_id != null)
				cs.delete(sys_id, dev_conf);
		}
	}
}
