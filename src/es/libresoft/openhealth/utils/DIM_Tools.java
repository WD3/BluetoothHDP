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
package es.libresoft.openhealth.utils;

import org.bn.annotations.ASN1OctetString;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AbsoluteTimeAdjust;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.BITS_16;
import ieee_11073.part_20601.asn1.BITS_32;
import ieee_11073.part_20601.asn1.BaseOffsetTime;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.BasicNuObsValueCmp;
import ieee_11073.part_20601.asn1.BatMeasure;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfirmMode;
import ieee_11073.part_20601.asn1.EnumObsValue;
import ieee_11073.part_20601.asn1.EnumPrintableString;
import ieee_11073.part_20601.asn1.FLOAT_Type;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.HANDLEList;
import ieee_11073.part_20601.asn1.HandleAttrValMap;
import ieee_11073.part_20601.asn1.HighResRelativeTime;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.INT_U32;
import ieee_11073.part_20601.asn1.InstNumber;
import ieee_11073.part_20601.asn1.MdsTimeInfo;
import ieee_11073.part_20601.asn1.MeasurementStatus;
import ieee_11073.part_20601.asn1.MetricIdList;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.MetricStructureSmall;
import ieee_11073.part_20601.asn1.NomPartition;
import ieee_11073.part_20601.asn1.NuObsValue;
import ieee_11073.part_20601.asn1.NuObsValueCmp;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.OperationalState;
import ieee_11073.part_20601.asn1.PersonId;
import ieee_11073.part_20601.asn1.PmSegmentEntryMap;
import ieee_11073.part_20601.asn1.PmStoreCapab;
import ieee_11073.part_20601.asn1.PowerStatus;
import ieee_11073.part_20601.asn1.ProductionSpec;
import ieee_11073.part_20601.asn1.RegCertDataList;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.SaSpec;
import ieee_11073.part_20601.asn1.ScaleRangeSpec16;
import ieee_11073.part_20601.asn1.ScaleRangeSpec32;
import ieee_11073.part_20601.asn1.ScaleRangeSpec8;
import ieee_11073.part_20601.asn1.SegmentStatistics;
import ieee_11073.part_20601.asn1.SimpleNuObsValue;
import ieee_11073.part_20601.asn1.SimpleNuObsValueCmp;
import ieee_11073.part_20601.asn1.StoSampleAlg;
import ieee_11073.part_20601.asn1.SupplementalTypeList;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.asn1.TypeVerList;

public class DIM_Tools {


	public static final String byteArrayToString (byte[] id){
		int length = id.length;
		String s = "";
		for (int i=0; i< length; i++){
			s += (char)id[i];
		}
		return s;
	}

	public static final String getAttributeName (int attrId){
		switch (attrId){
		case Nomenclature.MDC_ATTR_CONFIRM_MODE : return "Confirm-Mode";
		case Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT : return "Confirm-Timeout";
		case Nomenclature.MDC_ATTR_ID_HANDLE : return "Handle";
		case Nomenclature.MDC_ATTR_ID_INSTNO : return "Instance-Number";
		case Nomenclature.MDC_ATTR_ID_LABEL_STRING : return "Label-String";
		case Nomenclature.MDC_ATTR_ID_MODEL : return "System-Model";
		case Nomenclature.MDC_ATTR_ID_PHYSIO : return "Metric-Id";
		case Nomenclature.MDC_ATTR_ID_PROD_SPECN : return "Production-Specification";
		case Nomenclature.MDC_ATTR_ID_TYPE : return "Type";
		case Nomenclature.MDC_ATTR_METRIC_STORE_CAPAC_CNT : return "Store-Capacity-Count";
		case Nomenclature.MDC_ATTR_METRIC_STORE_SAMPLE_ALG : return "Store-Sample-Algorithm";
		case Nomenclature.MDC_ATTR_METRIC_STORE_USAGE_CNT : return "Store-Usage-Count";
		case Nomenclature.MDC_ATTR_MSMT_STAT : return "Measurement-Status";
		case Nomenclature.MDC_ATTR_NU_ACCUR_MSMT : return "Accuracy";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS : return "Compound-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS : return "Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NUM_SEG : return "Number-Of-Segments";
		case Nomenclature.MDC_ATTR_OP_STAT : return "Operational-State";
		case Nomenclature.MDC_ATTR_POWER_STAT : return "Power-Status";
		case Nomenclature.MDC_ATTR_SA_SPECN : return "Sa-Specification";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I16 : return "Scale-and-Range-Specification_I16";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I32 : return "Scale-and-Range-Specification_I32";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I8 : return "Scale-and-Range-Specification_I8";
		case Nomenclature.MDC_ATTR_SCAN_REP_PD : return "Reporting-Interval";
		case Nomenclature.MDC_ATTR_SEG_USAGE_CNT : return "Segment-Usage-Count";
		case Nomenclature.MDC_ATTR_SYS_ID : return "System-Id";
		case Nomenclature.MDC_ATTR_SYS_TYPE : return "System-Type";
		case Nomenclature.MDC_ATTR_TIME_ABS : return "Date-and-Time";
		case Nomenclature.MDC_ATTR_TIME_BATT_REMAIN : return "Remaining-Battery-Time";
		case Nomenclature.MDC_ATTR_TIME_END_SEG : return "Segment-End-Abs-Time";
		case Nomenclature.MDC_ATTR_TIME_PD_SAMP : return "Sample-Period";
		case Nomenclature.MDC_ATTR_TIME_REL : return "Relative-Time";
		case Nomenclature.MDC_ATTR_TIME_STAMP_ABS : return "Absolute-Time-Stamp";
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL : return "Relative-Time-Stamp";
		case Nomenclature.MDC_ATTR_TIME_START_SEG : return "Segment-Start-Abs-Time";
		case Nomenclature.MDC_ATTR_TX_WIND : return "Transmit-Window";
		case Nomenclature.MDC_ATTR_UNIT_CODE: return "Unit-Code";
		case Nomenclature.MDC_ATTR_UNIT_LABEL_STRING : return "Unit-LabelString";
		case Nomenclature.MDC_ATTR_VAL_BATT_CHARGE : return "Battery-Level";
		case Nomenclature.MDC_ATTR_VAL_ENUM_OBS : return "Enum-Observed-Value";
		case Nomenclature.MDC_ATTR_TIME_REL_HI_RES : return "HiRes-Relative-Time";
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL_HI_RES : return "HiRes-Time-Stamp";
		case Nomenclature.MDC_ATTR_DEV_CONFIG_ID: return "Dev-Configuration-Id";
		case Nomenclature.MDC_ATTR_MDS_TIME_INFO : return "Mds-Time-Info";
		case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL: return "Metric-Spec-Small";
		case Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF : return "Source-Handle-Reference";
		case Nomenclature.MDC_ATTR_SIMP_SA_OBS_VAL : return "Simple-Sa-Observed-Value";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID : return "Enum-Observed-Value-Simple-OID";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR : return "Enum-Observed-Value-Simple-Str";
		case Nomenclature.MDC_ATTR_REG_CERT_DATA_LIST : return "Reg-Cert-Data-List";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC : return "Basic-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_PM_STORE_CAPAB : return "PM-Store-Capab";
		case Nomenclature.MDC_ATTR_PM_SEG_MAP : return "PM-Segment-Entry-Map";
		case Nomenclature.MDC_ATTR_PM_SEG_PERSON_ID : return "PM-Seg-Person-Id";
		case Nomenclature.MDC_ATTR_SEG_STATS : return "Segment-Statistics";
		//case Nomenclature.MDC_ATTR_SEG_FIXED_DATA : return N/A;
		//case Nomenclature.MDC_ATTR_PM_SEG_ELEM_STAT_ATTR : return null; (Not used)
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP : return "Scan-Handle-Attr-Val-Map";
		case Nomenclature.MDC_ATTR_SCAN_REP_PD_MIN : return "Min-Reporting-Interval";
		case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP: return "Attribute-Value-Map";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP : return "Simple-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_PM_STORE_LABEL_STRING : return "PM-Store-Label";
		case Nomenclature.MDC_ATTR_PM_SEG_LABEL_STRING : return "Segment-Label";
		case Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE : return "Measure-Active-Period";
		case Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST: return "System-Type-Spec-List";
		case Nomenclature.MDC_ATTR_METRIC_ID_PART : return "Metric-Id-Partition";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_PART : return "Enum-Observed-Value-Partition";
		case Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES : return "Supplemental-Types";
		case Nomenclature.MDC_ATTR_TIME_ABS_ADJUST : return "Date-and-Time-Adjustment";
		case Nomenclature.MDC_ATTR_CLEAR_TIMEOUT : return "Clear-Timeout";
		case Nomenclature.MDC_ATTR_TRANSFER_TIMEOUT : return "Transfer-Timeout";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR : return "Enum-Observed-Value-Simple-Bit-Str";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR : return "Enum-Observed-Value-Basic-Bit-Str";
		case Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL : return "Metric-Structure-Small";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP : return "Compund-Simple-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC : return "Compund-Basic-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST : return "Metric-Id-List";
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_LIST : return "Scan-Handle-List";
		case Nomenclature.MDC_ATTR_TIME_BO : return "Base-Offset-Time";
		default: return null;
		}
	}

	public static final Class<?> getAttributeClass (int attrId){
		switch (attrId){
		case Nomenclature.MDC_ATTR_CONFIRM_MODE : return ConfirmMode.class;
		case Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_ID_HANDLE : return HANDLE.class;
		case Nomenclature.MDC_ATTR_ID_INSTNO : return InstNumber.class;
		case Nomenclature.MDC_ATTR_ID_LABEL_STRING : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_ID_MODEL : return SystemModel.class;
		case Nomenclature.MDC_ATTR_ID_PHYSIO : return OID_Type.class;
		case Nomenclature.MDC_ATTR_ID_PROD_SPECN : return ProductionSpec.class;
		case Nomenclature.MDC_ATTR_ID_TYPE : return TYPE.class;
		case Nomenclature.MDC_ATTR_METRIC_STORE_CAPAC_CNT : return INT_U32.class;
		case Nomenclature.MDC_ATTR_METRIC_STORE_SAMPLE_ALG : return StoSampleAlg.class;
		case Nomenclature.MDC_ATTR_METRIC_STORE_USAGE_CNT : return INT_U32.class;
		case Nomenclature.MDC_ATTR_MSMT_STAT : return MeasurementStatus.class;
		case Nomenclature.MDC_ATTR_NU_ACCUR_MSMT : return FLOAT_Type.class;
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS : return NuObsValueCmp.class;
		case Nomenclature.MDC_ATTR_NU_VAL_OBS : return NuObsValue.class;
		case Nomenclature.MDC_ATTR_NUM_SEG : return INT_U16.class;
		case Nomenclature.MDC_ATTR_OP_STAT : return OperationalState.class;
		case Nomenclature.MDC_ATTR_POWER_STAT : return PowerStatus.class;
		case Nomenclature.MDC_ATTR_SA_SPECN : return SaSpec.class;
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I16 : return ScaleRangeSpec16.class;
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I32 : return ScaleRangeSpec32.class;
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I8 : return ScaleRangeSpec8.class;
		case Nomenclature.MDC_ATTR_SCAN_REP_PD : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_SEG_USAGE_CNT : return INT_U32.class;
		case Nomenclature.MDC_ATTR_SYS_ID : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_SYS_TYPE : return TYPE.class;
		case Nomenclature.MDC_ATTR_TIME_ABS : return AbsoluteTime.class;
		case Nomenclature.MDC_ATTR_TIME_BATT_REMAIN : return BatMeasure.class;
		case Nomenclature.MDC_ATTR_TIME_END_SEG : return AbsoluteTime.class;
		case Nomenclature.MDC_ATTR_TIME_PD_SAMP : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_TIME_REL : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_TIME_STAMP_ABS : return AbsoluteTime.class;
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_TIME_START_SEG : return AbsoluteTime.class;
		case Nomenclature.MDC_ATTR_TX_WIND : return INT_U16.class;
		case Nomenclature.MDC_ATTR_UNIT_CODE: return OID_Type.class;
		case Nomenclature.MDC_ATTR_UNIT_LABEL_STRING : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_VAL_BATT_CHARGE : return INT_U16.class;
		case Nomenclature.MDC_ATTR_VAL_ENUM_OBS : return EnumObsValue.class;
		case Nomenclature.MDC_ATTR_TIME_REL_HI_RES : return HighResRelativeTime.class;
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL_HI_RES : return HighResRelativeTime.class;
		case Nomenclature.MDC_ATTR_DEV_CONFIG_ID: return ConfigId.class;
		case Nomenclature.MDC_ATTR_MDS_TIME_INFO : return MdsTimeInfo.class;
		case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL: return MetricSpecSmall.class;
		case Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF : return HANDLE.class;
		case Nomenclature.MDC_ATTR_SIMP_SA_OBS_VAL : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID : return OID_Type.class;
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR : return EnumPrintableString.class;
		case Nomenclature.MDC_ATTR_REG_CERT_DATA_LIST : return RegCertDataList.class;
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC : return BasicNuObsValue.class;
		case Nomenclature.MDC_ATTR_PM_STORE_CAPAB : return PmStoreCapab.class;
		case Nomenclature.MDC_ATTR_PM_SEG_MAP : return PmSegmentEntryMap.class;
		case Nomenclature.MDC_ATTR_PM_SEG_PERSON_ID : return PersonId.class;
		case Nomenclature.MDC_ATTR_SEG_STATS : return SegmentStatistics.class;
		//case Nomenclature.MDC_ATTR_SEG_FIXED_DATA : return N/A;
		//case Nomenclature.MDC_ATTR_PM_SEG_ELEM_STAT_ATTR : return null; //Not used
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP : return HandleAttrValMap.class;
		case Nomenclature.MDC_ATTR_SCAN_REP_PD_MIN : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP: return AttrValMap.class;
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP : return SimpleNuObsValue.class;
		case Nomenclature.MDC_ATTR_PM_STORE_LABEL_STRING : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_PM_SEG_LABEL_STRING : return ASN1OctetString.class;
		case Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE : return FLOAT_Type.class;
		case Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST: return TypeVerList.class;
		case Nomenclature.MDC_ATTR_METRIC_ID_PART : return NomPartition.class;
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_PART : return NomPartition.class;
		case Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES : return SupplementalTypeList.class;
		case Nomenclature.MDC_ATTR_TIME_ABS_ADJUST : return AbsoluteTimeAdjust.class;
		case Nomenclature.MDC_ATTR_CLEAR_TIMEOUT : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_TRANSFER_TIMEOUT : return RelativeTime.class;
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR : return BITS_32.class;
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR : return BITS_16.class;
		case Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL : return MetricStructureSmall.class;
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP : return SimpleNuObsValueCmp.class;
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC : return BasicNuObsValueCmp.class;
		case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST : return MetricIdList.class;
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_LIST : return HANDLEList.class;
		case Nomenclature.MDC_ATTR_TIME_BO : return BaseOffsetTime.class;
		default: return null;
		}
	}
}
