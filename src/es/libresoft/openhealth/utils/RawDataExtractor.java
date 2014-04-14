/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>
Author: Jorge Fernández González <jfernandez@libresoft.es>

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

package es.libresoft.openhealth.utils;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.BITS_32;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.BasicNuObsValueCmp;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.INT_U32;
import ieee_11073.part_20601.asn1.MeasurementStatus;
import ieee_11073.part_20601.asn1.MetricIdList;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.MetricStructureSmall;
import ieee_11073.part_20601.asn1.NomPartition;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.SupplementalTypeList;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DIM;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.bn.CoderFactory;
import org.bn.IDecoder;
import org.bn.annotations.ASN1OctetString;

import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.logging.Logging;

public class RawDataExtractor {
	private int index;
	private byte[] raw;

	public RawDataExtractor (byte[] raw_data){
		raw = raw_data;
		index = 0;
	}

	public byte[] getData (int len){
		if ((index + len)>raw.length)
				return null;
		byte[] data = new byte[len];
		for (int i = 0; i<len; i++)
			data[i]=raw[index++];
		return data;
	}

	public boolean hasMoreData() {
		return index < raw.length;
	}

	public int availableData() {
		return raw.length - index;
	}

	public static <T> T decodeRawData(int attrId, byte[] data, String eRules) throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		//Decode AttrValMap using accorded enc_rules
		IDecoder decoder = CoderFactory.getInstance().newDecoder(eRules);
		Class<?> attrClass = DIM_Tools.getAttributeClass(attrId);
		if (attrClass == ASN1OctetString.class) {
			OctetStringASN1 ostring = decoder.decode(input,  OctetStringASN1.class);
			return (T)ostring.getValue();
		} else
			return (T)decoder.decode(input, DIM_Tools.getAttributeClass(attrId));
	}

	public static boolean updateAttrValue(DIM obj, int attId, Object data){
		try{
			switch(attId){
			// Metric attributes whose values could be modified:
			case Nomenclature.MDC_ATTR_ID_HANDLE:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE, (HANDLE)data));
				return true;
			case Nomenclature.MDC_ATTR_ID_TYPE:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ID_TYPE, (TYPE)data));
				return true;
			case Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES, (SupplementalTypeList)data));
				return true;
			case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, (MetricSpecSmall)data));
				return true;
			case Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL, (MetricStructureSmall)data));
				return true;
			case Nomenclature.MDC_ATTR_MSMT_STAT:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_MSMT_STAT, (MeasurementStatus)data));
				return true;
			case Nomenclature.MDC_ATTR_ID_PHYSIO:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ID_PHYSIO, (OID_Type)data));
				return true;
			case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ID_PHYSIO_LIST, (MetricIdList)data));
				return true;
			case Nomenclature.MDC_ATTR_METRIC_ID_PART:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_METRIC_ID_PART, (NomPartition)data));
				return true;
			case Nomenclature.MDC_ATTR_UNIT_CODE:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE, (OID_Type)data));
				return true;
			case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, (AttrValMap)data));
				return true;
			case Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF, (HANDLE)data));
				return true;
			case Nomenclature.MDC_ATTR_ID_LABEL_STRING:
				//obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_ID_LABEL_STRING, (OCTETSTRING)data));
				return false;
			case Nomenclature.MDC_ATTR_UNIT_LABEL_STRING:
				//obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_UNIT_LABEL_STRING, (OCTETSTRING)data));
				return false;
			case Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE:
				obj.addAttribute(new Attribute(Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE, (FloatType)data));
				return true;
			default:
				return false;
			}
		}catch(InvalidAttributeException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static <T> T decodeMeasure(int attrId, byte[] data, String eRules) throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		//Decode AttrValMap using accorded enc_rules
		IDecoder decoder = CoderFactory.getInstance().newDecoder(eRules);
		switch(attrId){
			// Numeric attributes whose values could be modified:
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP:
				Logging.debug("MDC_ATTR_NU_VAL_OBS_SIMP");
				INT_U32 iu = decoder.decode(input, INT_U32.class);
				FloatType ft = new FloatType(iu.getValue());
				Logging.debug("Measure: " + ft.doubleValueRepresentation() + ", " + ft);
				return (T)ft;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_NU_CMPD_VAL_OBS_SIMP");
				break;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC:
				Logging.debug("MDC_ATTR_NU_VAL_OBS_BASIC");
				INT_U16 iu2 = decoder.decode(input, INT_U16.class);
				SFloatType sft = new SFloatType(iu2.getValue());
				Logging.debug("Measure: " + sft.doubleValueRepresentation() + ", " + sft);
				return (T)sft;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC:
				Logging.debug("MDC_ATTR_NU_CMPD_VAL_OBS_BASIC");
				BasicNuObsValueCmp cmpVal = decoder.decode(input, BasicNuObsValueCmp.class);
				Iterator<BasicNuObsValue> it = cmpVal.getValue().iterator();
				ArrayList<SFloatType> measures = new ArrayList<SFloatType>();
				while (it.hasNext()) {
					BasicNuObsValue value = it.next();
					SFloatType ms = new SFloatType(value.getValue().getValue());
					Logging.debug("Measure: " + ms.doubleValueRepresentation() + ", " + ms);
					measures.add(ms);
				}
				return (T)measures;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_NU_VAL_OBS");
				break;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_NU_CMPD_VAL_OBS");
				break;
			case Nomenclature.MDC_ATTR_NU_ACCUR_MSMT:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_NU_ACCUR_MSMT");
				break;
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID:
				Logging.debug("MDC_ATTR_ENUM_OBS_VAL_SIMP_OID");
				OID_Type oid = decoder.decode(input, OID_Type.class);
				Logging.debug("Measure oid_type: " + oid.getValue().getValue());
				return (T)oid.getValue().getValue();
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR:
				Logging.debug("MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR");
				BITS_32 bits32 = decoder.decode(input, BITS_32.class);
				Logging.debug("Measure: " + ASN1_Tools.getHexString(bits32.getValue().getValue()));
				return (T)bits32.getValue().getValue();
			case Nomenclature.MDC_ATTR_TIME_ABS:
			case Nomenclature.MDC_ATTR_TIME_STAMP_ABS:
				Logging.debug("MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR");
				AbsoluteTime datetime = decoder.decode(input, AbsoluteTime.class);
				return (T)datetime;
			case Nomenclature.MDC_ATTR_TIME_STAMP_BO: // related to BaseOffsetTime
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_TIME_STAMP_BO");
				break;
			case Nomenclature.MDC_ATTR_TIME_STAMP_REL:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_TIME_STAMP_REL");
				break;
			case Nomenclature.MDC_ATTR_TIME_STAMP_REL_HI_RES:
				Logging.debug("TODO decodeMeasure(): MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR");
				break;
			case Nomenclature.MDC_ATTR_SIMP_SA_OBS_VAL:
				Logging.debug("MDC_ATTR_SIMP_SA_OBS_VAL");
				OctetStringASN1 ostring = decoder.decode(input, OctetStringASN1.class);
				Logging.debug("Measure: " + ASN1_Tools.getHexString(ostring.getValue()));
				return (T)ostring.getValue();
			default:
				Logging.debug("TODO decodeMeasure(): DEFAULT");
		}
		throw new Exception ("Attribute " + attrId + " unknown.");
	}

}
