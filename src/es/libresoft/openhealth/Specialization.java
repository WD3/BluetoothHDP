package es.libresoft.openhealth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import es.libresoft.openhealth.utils.ASN1_Tools;
import ieee_11073.part_20601.asn1.AVA_Type;
import ieee_11073.part_20601.asn1.AttributeList;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.phd.dim.Attribute;

public abstract class Specialization {
	
	protected DeviceConfig dev_conf;
	protected Collection<ConfigObject> configdata;
	protected Collection<Map<HANDLE, Object>> Measures; 
	
	
	public Collection<ConfigObject> getConfigdata() {
		return configdata;
	}
	public byte[] getSystem_id() {
		return dev_conf.getSystemId();
	}
	public ConfigId getDev_config_id() {
		return new ConfigId(dev_conf.getPhdId());
	}
	public DeviceConfig getDev_conf() {
		return dev_conf;
	}
	public Collection<Map<HANDLE, Object>> getMeasures() {
		return Measures;
	}
	protected AttributeList getAttributeList (Hashtable<Integer,Attribute> attribs, String enc_rules) throws Exception {
		AttributeList attrList = new AttributeList(new ArrayList<AVA_Type>());
		Iterator<Attribute> it = attribs.values().iterator();
		Attribute attr;
		while(it.hasNext()){
			attr = it.next();
			AVA_Type ava = new AVA_Type();
			OID_Type oid = new OID_Type();
			oid.setValue(new INT_U16(attr.getAttributeID()));
			ava.setAttribute_id(oid);
			byte[] value = ASN1_Tools.encodeData(attr.getAttributeType(), enc_rules);
			ava.setAttribute_value(value);
			attrList.add(ava);
		}
		return attrList;
	}
}
