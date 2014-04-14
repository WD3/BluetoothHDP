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

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AVA_Type;
import ieee_11073.part_20601.asn1.AttributeList;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.OID_Type;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.bn.annotations.ASN1OctetString;

import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.ConfigStorageFactory;
import es.libresoft.openhealth.storage.StorageException;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.DIM_Tools;
import es.libresoft.openhealth.utils.OctetStringASN1;

public abstract class DIM {

	private static final String MDER_ENCODING = "MDER";

	private MDS mds;

	/*AttributeID,Attribute*/
	protected Hashtable<Integer,Attribute> attributeList;

	public DIM () {}

	public DIM (Hashtable<Integer,Attribute> attributes) throws InvalidAttributeException {
		if (attributes==null) {
			throw new InvalidAttributeException("Not attributes has been declared in MDS class.");
		} else if (attributes.isEmpty()) {
			throw new InvalidAttributeException("Not attributes has been declared in MDS class.");
		}
		this.attributeList = new Hashtable<Integer,Attribute>();
		checkAttributes(attributes);
		addCheckedAttributes(attributes);
	}

	public Attribute getAttribute(int attributeId){
		return attributeList.get(new Integer(attributeId));
	}

	public Attribute getAttribute(String friendlyName){
		Iterator<Attribute> i =  attributeList.values().iterator();
		Attribute attr;
		while (i.hasNext()) {
			attr = (Attribute)i.next();
			if (attr.getAttributeName().equals(friendlyName))
					return attr;
		}
		return null;
	}

	public Hashtable<Integer,Attribute> getAttributes() {
		return attributeList;
	}

	public void addAttribute(Attribute attr){
		attributeList.put(new Integer(attr.getAttributeID()), attr);
	}

	public abstract int getNomenclatureCode ();

	protected abstract void checkAttributes(Hashtable<Integer,Attribute> attributes) throws InvalidAttributeException;


	public void setMDS(MDS mds) {
		this.mds = mds;
	}

	public MDS getMDS() {
		return this.mds;
	}
	
	public AttributeList getAttributeList (){
		AttributeList attrList = new AttributeList(new ArrayList<AVA_Type>());
		Iterator<Attribute> it = attributeList.values().iterator();
		Attribute attr;
		while(it.hasNext()){
			attr = it.next();
			AVA_Type ava = new AVA_Type();
			OID_Type oid = new OID_Type();
			oid.setValue(new INT_U16(attr.getAttributeID()));
			ava.setAttribute_id(oid);
			byte[] value;
			try {
				value = ASN1_Tools.encodeData(attr.getAttributeType(), MDER_ENCODING);
				ava.setAttribute_value(value);
				attrList.add(ava);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return attrList;
	}

	//-------------------------------------------PRIVATE------------------------------------------------------
	protected void addCheckedAttributes(Hashtable<Integer,Attribute> attributes){
		/* Add attributes to attribute list */
		Iterator<Attribute> i = attributes.values().iterator();
		while (i.hasNext()){
			addAttribute(i.next());
		}
	}

	protected Hashtable<Integer,Attribute> getAttributes (AttributeList attrList, String enc_rules) throws Exception {
		Hashtable<Integer,Attribute> attribs = new Hashtable<Integer,Attribute>();
		Iterator<AVA_Type> it = attrList.getValue().iterator();
		AVA_Type ava;
		Object type;

		while (it.hasNext()){
			ava = it.next();
			Class<?> attrClass = DIM_Tools.getAttributeClass(ava.getAttribute_id().getValue().getValue());
			if (attrClass == null) {
				Logging.debug("Error: Can't get Attribute " + ava.getAttribute_id().getValue().getValue());
				continue;
			}

			if (attrClass == ASN1OctetString.class) {
				OctetStringASN1 ostring = ASN1_Tools.decodeData(ava.getAttribute_value(), OctetStringASN1.class, enc_rules);
				type = ostring.getValue();
			} else
				type = ASN1_Tools.decodeData(ava.getAttribute_value(), attrClass, enc_rules);

			Attribute attr = new Attribute(ava.getAttribute_id().getValue().getValue(), type);
			attribs.put(new Integer(ava.getAttribute_id().getValue().getValue()), attr);
		}
		return attribs;
	}

	public void storeConfiguration(byte[] sys_id, DeviceConfig config) throws StorageException{
		HANDLE handle = (HANDLE) getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();

		try {
			ConfigStorage cs = ConfigStorageFactory.getDefaultConfigStorage();
			ConfigObject data = new ConfigObject();

			data.setObj_handle(handle);

			ArrayList<AVA_Type> list = new ArrayList<AVA_Type>();
			Iterator<Integer> it = attributeList.keySet().iterator();
			while (it.hasNext()) {
				Integer id = it.next();

				AVA_Type ava = new AVA_Type();
				OID_Type oid = new OID_Type();
				oid.setValue(new INT_U16(id));
				ava.setAttribute_id(oid);

				ava.setAttribute_value(ASN1_Tools.encodeData(attributeList.get(id).getAttributeType(), MDER_ENCODING));
				list.add(ava);
			}

			OID_Type obj_class = new OID_Type();
			obj_class.setValue(new INT_U16(getNomenclatureCode()));
			data.setObj_class(obj_class);

			data.setAttributes(new AttributeList(list));

			cs.store(sys_id, config, data);
		} catch (StorageException e) {
			throw e;
		} catch (Exception e) {
			Logging.error("Configuration won't be stored for object " + handle.getValue().getValue());
			throw new StorageException(e);
		}
	}
}
