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

import Config.ManagerConfig;

public class DeviceConfig {

	private int data_proto_id;
	private int phdid;
	private String enc_rules;
	private byte[] protocol_version;
	private byte[] nomenclature_version;
	private int data_req_init_agent_count;
	private int data_req_init_manager_count;
	private byte[] data_req_mode_flags;
	
	private byte[] system_id;
	private byte[] system_type;
	private byte[] assoc_version;
	private byte[] functional_units;
	
	/* Supported encoding rules */
	public static byte[] enc_mder = {(byte)128, (byte)0}; /*0x8000*/
	//public byte[] enc_xer = {(byte)64, (byte)0}; /*0x4000*/
	public static byte[] enc_per = {(byte)32, (byte)0}; /*0x2000*/
	
	
	public DeviceConfig(byte[]system_id,byte[]system_type,byte[]assoc_version,int proto_id, int phdid, String enc_rules, byte[] p_version, byte[] n_version,
			int driac, int drimc, byte[] drmf,byte[] functional_units)
	{
		this.system_id = system_id;
		this.system_type = system_type;
		this.assoc_version = assoc_version;
		this.data_proto_id = proto_id;
		this.phdid = phdid;
		this.enc_rules = enc_rules;
		this.protocol_version = p_version;
		this.nomenclature_version = n_version;
		this.data_req_init_agent_count = driac;
		this.data_req_init_manager_count = drimc;
		this.data_req_mode_flags = drmf;
		this.functional_units = functional_units;
	}

	public int getDataProtoId(){return this.data_proto_id;}

	public int getPhdId(){return this.phdid;}

	public String getEncondigRules(){return this.enc_rules;}

	public byte[] getEncondigRulesToArray(){
		if (enc_rules.equalsIgnoreCase("PER"))
			return enc_per;
		//else if (enc_rules.equalsIgnoreCase("XER"))
		//	return ManagerConfig.enc_xer;
		/* Default MDER encoding */
		return enc_mder;
	}
	public byte[] getProtocolVersion(){return this.protocol_version;}
	public byte[] getNomenclatureVersion(){return this.nomenclature_version;}
	public int getDataReqInitAgentCount(){return this.data_req_init_agent_count;}
	public int getDataReqInitManagerCount(){return this.data_req_init_manager_count;}
	public byte[] getDataReqModeFlags(){return this.data_req_mode_flags;}
	public byte[] getSystemId(){return this.system_id;}
	public byte[] getSystemType(){return this.system_type;}
	public byte[] getAssocVersion(){return this.assoc_version;}
	public byte[] getFunctionalUnits(){return this.functional_units;}
}
