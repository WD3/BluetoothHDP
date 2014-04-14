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

package es.libresoft.openhealth.storage;

import java.util.Collection;

import ieee_11073.part_20601.asn1.ConfigObject;
import es.libresoft.openhealth.DeviceConfig;

public interface ConfigStorage {

	/**
	 * Stores the DIM object represented in a GetResultSimple structure
	 * which is a HANDLE and a list of attributes.
	 *
	 * If the same object HANDLE is given inside of data parameter the previous
	 * data should be deleted.
	 *
	 * The information stored will be recovered with @ref recover method
	 *
	 * @param sys_id The device identification
	 * @param config The device configuration
	 * @param data All DIM object attributes and its HANDLE
	 */
	public void store(byte[] sys_id, DeviceConfig config, ConfigObject data) throws StorageException;

	/**
	 * Gets all the DIM objects previously stored using @ref store for the device
	 * identified by the system id and configuration given.
	 *
	 * @param sys_id The system id of the device
	 * @param config The configuration of the device
	 * @return A collection with all the DIM objects previously stored.
	 */
	public Collection<ConfigObject> recover(byte[] sys_id, DeviceConfig config) throws StorageNotFoundException;

	/**
	 * Deletes all the configurations for the given device.
	 *
	 * @param sys_id The system id of the configuration that will be deleted
	 * @param config The configuration that will be deleted for the given system id.
	 */
	public void delete(byte[] sys_id, DeviceConfig config);
}
