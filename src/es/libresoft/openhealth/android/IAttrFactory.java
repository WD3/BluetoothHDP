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

package es.libresoft.openhealth.android;

import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AbsoluteTimeAdjust;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.AuthBodyAndStrucType;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.BatMeasure;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.FLOAT_Type;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.HighResRelativeTime;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.INT_U32;
import ieee_11073.part_20601.asn1.InstNumber;
import ieee_11073.part_20601.asn1.MdsTimeCapState;
import ieee_11073.part_20601.asn1.MdsTimeInfo;
import ieee_11073.part_20601.asn1.MetricIdList;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.NomPartition;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.OperationalState;
import ieee_11073.part_20601.asn1.PersonId;
import ieee_11073.part_20601.asn1.PmSegmentEntryMap;
import ieee_11073.part_20601.asn1.PmStoreCapab;
import ieee_11073.part_20601.asn1.PowerStatus;
import ieee_11073.part_20601.asn1.PrivateOid;
import ieee_11073.part_20601.asn1.ProdSpecEntry;
import ieee_11073.part_20601.asn1.ProductionSpec;
import ieee_11073.part_20601.asn1.RegCertData;
import ieee_11073.part_20601.asn1.RegCertDataList;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.SegmEntryElem;
import ieee_11073.part_20601.asn1.SegmEntryElemList;
import ieee_11073.part_20601.asn1.SegmEntryHeader;
import ieee_11073.part_20601.asn1.SegmStatType;
import ieee_11073.part_20601.asn1.SegmentStatisticEntry;
import ieee_11073.part_20601.asn1.SegmentStatistics;
import ieee_11073.part_20601.asn1.StoSampleAlg;
import ieee_11073.part_20601.asn1.SupplementalTypeList;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.asn1.TypeVer;
import ieee_11073.part_20601.asn1.TypeVerList;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.util.ArrayList;
import java.util.Iterator;

import org.bn.types.BitString;

import android.os.Parcelable;
import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.android.aidl.types.IAbsoluteTime;
import es.libresoft.openhealth.android.aidl.types.IAbsoluteTimeAdjust;
import es.libresoft.openhealth.android.aidl.types.IAttrValMap;
import es.libresoft.openhealth.android.aidl.types.IAttrValMapEntry;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IAuthBodyAndStrucType;
import es.libresoft.openhealth.android.aidl.types.IBITSTRING;
import es.libresoft.openhealth.android.aidl.types.IBasicNuObsValue;
import es.libresoft.openhealth.android.aidl.types.IBatMeasure;
import es.libresoft.openhealth.android.aidl.types.IConfigId;
import es.libresoft.openhealth.android.aidl.types.IFLOAT_Type;
import es.libresoft.openhealth.android.aidl.types.IHANDLE;
import es.libresoft.openhealth.android.aidl.types.IHighResRelativeTime;
import es.libresoft.openhealth.android.aidl.types.IINT_U16;
import es.libresoft.openhealth.android.aidl.types.IINT_U32;
import es.libresoft.openhealth.android.aidl.types.IInstNumber;
import es.libresoft.openhealth.android.aidl.types.IMdsTimeCapState;
import es.libresoft.openhealth.android.aidl.types.IMdsTimeInfo;
import es.libresoft.openhealth.android.aidl.types.IMetricIdList;
import es.libresoft.openhealth.android.aidl.types.IMetricSpecSmall;
import es.libresoft.openhealth.android.aidl.types.INomPartition;
import es.libresoft.openhealth.android.aidl.types.IOCTETSTRING;
import es.libresoft.openhealth.android.aidl.types.IOID_Type;
import es.libresoft.openhealth.android.aidl.types.IOperationalState;
import es.libresoft.openhealth.android.aidl.types.IPersonId;
import es.libresoft.openhealth.android.aidl.types.IPmSegmentEntryMap;
import es.libresoft.openhealth.android.aidl.types.IPmStoreCapab;
import es.libresoft.openhealth.android.aidl.types.IPowerStatus;
import es.libresoft.openhealth.android.aidl.types.IPrivateOid;
import es.libresoft.openhealth.android.aidl.types.IProductionSpec;
import es.libresoft.openhealth.android.aidl.types.IProductionSpecEntry;
import es.libresoft.openhealth.android.aidl.types.IRegCertData;
import es.libresoft.openhealth.android.aidl.types.IRegCertDataList;
import es.libresoft.openhealth.android.aidl.types.IRelativeTime;
import es.libresoft.openhealth.android.aidl.types.ISFloatType;
import es.libresoft.openhealth.android.aidl.types.ISegmEntryElem;
import es.libresoft.openhealth.android.aidl.types.ISegmEntryElemList;
import es.libresoft.openhealth.android.aidl.types.ISegmEntryHeader;
import es.libresoft.openhealth.android.aidl.types.ISegmStatType;
import es.libresoft.openhealth.android.aidl.types.ISegmentStatisticEntry;
import es.libresoft.openhealth.android.aidl.types.ISegmentStatistics;
import es.libresoft.openhealth.android.aidl.types.IStoSampleAlg;
import es.libresoft.openhealth.android.aidl.types.ISupplementalTypeList;
import es.libresoft.openhealth.android.aidl.types.ISystemModel;
import es.libresoft.openhealth.android.aidl.types.ITYPE;
import es.libresoft.openhealth.android.aidl.types.ITypeVer;
import es.libresoft.openhealth.android.aidl.types.ITypeVerList;
import es.libresoft.openhealth.logging.Logging;


public class IAttrFactory {

	private static IHANDLE HANDLE2parcelable(HANDLE handle, int attrId) {
		IHANDLE ihandle = new IHANDLE(handle.getValue().getValue());
		return ihandle;
	}

	private static INomPartition NomPartition2parcelable(NomPartition partition, int attrId) {
		return new INomPartition(partition.getValue());
	}

	private static ITYPE TYPE2parcelable(TYPE type, int attrId) {
		INomPartition partition = NomPartition2parcelable(type.getPartition(), attrId);
		IOID_Type code = OID_Type2parcelable(type.getCode(), attrId);
		return new ITYPE(partition, code, AttributeUtils.partitionValue2string(partition.getNomPart(), code.getType()));
	}

	private static ISystemModel SystemModel2parcelable(SystemModel model, int attrId) {
		return new ISystemModel(new String(model.getManufacturer()), new String(model.getModel_number()));
	}

	private static IOCTETSTRING OCTETSTRING2parcelable(byte[] octetString, int attrId) {
		return new IOCTETSTRING(octetString);
	}

	private static IConfigId ConfigId2parcelable(ConfigId confId, int attrId) {
		return new IConfigId(confId.getValue());
	}

	private static IAttrValMapEntry AttrValMapEntry2parcelable(AttrValMapEntry entry, int attrId) {
		return new IAttrValMapEntry(OID_Type2parcelable(entry.getAttribute_id(), attrId),
						entry.getAttribute_len());
	}

	private static IAttrValMap AttrValMap2parcelable(AttrValMap valMap, int attrId) {
		ArrayList<IAttrValMapEntry> values = new ArrayList<IAttrValMapEntry>();
		Iterator<AttrValMapEntry> it = valMap.getValue().iterator();
		while (it.hasNext()) {
			AttrValMapEntry entry = it.next();
			values.add(AttrValMapEntry2parcelable(entry, attrId));
		}
		return new IAttrValMap(values);
	}

	private static IPrivateOid PrivateOid2parcelable(PrivateOid oid, int attrId) {
		return new IPrivateOid(oid.getValue().getValue());
	}

	private static IProductionSpecEntry AttrProdSpec2parcelable(ProdSpecEntry entry, int attrId) {
		return new IProductionSpecEntry(entry.getSpec_type(),
				PrivateOid2parcelable(entry.getComponent_id(), attrId),
				OCTETSTRING2parcelable(entry.getProd_spec(), attrId));
	}

	private static IProductionSpec AttrProductionSpec2parcelable(ProductionSpec spec, int attrId) {
		ArrayList<IProductionSpecEntry> values = new ArrayList<IProductionSpecEntry>();
		Iterator<ProdSpecEntry> it = spec.getValue().iterator();
		while (it.hasNext()) {
			ProdSpecEntry entry = it.next();
			values.add(AttrProdSpec2parcelable(entry, attrId));
		}
		return new IProductionSpec(values);
	}

	private static IBITSTRING BitString2parcelable(BitString bitstring, int attrId) {
		return new IBITSTRING(bitstring.getValue(), bitstring.getTrailBitsCnt());
	}

	private static IMdsTimeCapState MdsTimeCapState2parcelable(MdsTimeCapState capState, int attrId) {
		return new IMdsTimeCapState(BitString2parcelable(capState.getValue(), attrId));
	}

	private static IMdsTimeInfo MdsTimeInfo2parcelable(MdsTimeInfo timeInfo, int attrId) {
		return new IMdsTimeInfo(MdsTimeCapState2parcelable(timeInfo.getMds_time_cap_state(), attrId),
				OID_Type2parcelable(timeInfo.getTime_sync_protocol().getValue(), attrId),
				RelativeTime2parcelable(timeInfo.getTime_sync_accuracy(), attrId),
				timeInfo.getTime_resolution_abs_time(), timeInfo.getTime_resolution_rel_time(),
				timeInfo.getTime_resolution_high_res_time().getValue());
	}

	private static IAbsoluteTime AbsoluteTime2parcelable(AbsoluteTime absTime, int attrId) {
		return new IAbsoluteTime(absTime.getCentury().getValue(),
							absTime.getYear().getValue(),
							absTime.getMonth().getValue(),
							absTime.getDay().getValue(),
							absTime.getHour().getValue(),
							absTime.getMinute().getValue(),
							absTime.getSecond().getValue(),
							absTime.getSec_fractions().getValue());
	}

	private static IRelativeTime RelativeTime2parcelable(RelativeTime relTime, int attrId) {
		return new IRelativeTime(relTime.getValue().getValue());
	}

	private static IHighResRelativeTime HighResRelativeTime2parcelable(HighResRelativeTime relTime, int attrId) {
		return new IHighResRelativeTime(OCTETSTRING2parcelable(relTime.getValue(), attrId));
	}

	private static IAbsoluteTimeAdjust AbsoluteTimeAdjust2parcelable(AbsoluteTimeAdjust absTimeAdj, int attrId) {
		return new IAbsoluteTimeAdjust(OCTETSTRING2parcelable(absTimeAdj.getValue(), attrId));
	}

	private static IPowerStatus PowerStatus2parcelable(PowerStatus powerStatus, int attrId) {
		return new IPowerStatus(BitString2parcelable(powerStatus.getValue(), attrId));
	}

	private static IINT_U16 INT_U162parcelable(INT_U16 intu16, int attrId) {
		return new IINT_U16(intu16.getValue());
	}

	private static IStoSampleAlg StoSampleAlg2parcelable(StoSampleAlg ssa,
			int attrId) {
		return new IStoSampleAlg(ssa.getValue());
	}

	private static IPmStoreCapab PmStoreCapab2parcelable(PmStoreCapab pmsc,
			int attrId) {
		return new IPmStoreCapab(BitString2parcelable(pmsc.getValue(), attrId));
	}

	private static IINT_U32 INT_U322parcelable(INT_U32 intu32, int attrId) {
		return new IINT_U32(intu32.getValue());
	}

	private static IFLOAT_Type FLOAT_Type2parcelable(FLOAT_Type value, int attrId) {
		try {
			return new IFLOAT_Type(new FloatType(value.getValue().getValue()).doubleValueRepresentation());
		} catch (Exception e) {
			return null;
		}
	}

	private static IBatMeasure BatMeasure2parcelable(BatMeasure batMeasure, int attrId) {
		return new IBatMeasure(FLOAT_Type2parcelable(batMeasure.getValue(), attrId),
									OID_Type2parcelable(batMeasure.getUnit_(), attrId));
	}

	private static IAuthBodyAndStrucType AuthBodyAndStrucType2parcelable(AuthBodyAndStrucType bodyType, int attrId) {
		return new IAuthBodyAndStrucType(bodyType.getAuth_body().getValue(),
							bodyType.getAuth_body_struc_type().getValue());
	}

	private static IRegCertData RecCertData2parcelable(RegCertData certData, int attrId) {
		return new IRegCertData(AuthBodyAndStrucType2parcelable(certData.getAuth_body_and_struc_type(), attrId), null);
	}

	private static IRegCertDataList RegCertDataList2parcelable(RegCertDataList asnAttr, int attrId) {
		ArrayList<IRegCertData> values = new ArrayList<IRegCertData>();
		Iterator<RegCertData> it = asnAttr.getValue().iterator();
		while (it.hasNext()) {
			values.add(RecCertData2parcelable(it.next(), attrId));
		}
		return new IRegCertDataList(values);
	}

	private static ITypeVer TypeVer2parcelable(TypeVer ver, int attrId) {
		return new ITypeVer(OID_Type2parcelable(ver.getType(), attrId), ver.getVersion());
	}

	private static ITypeVerList TypeVerList2parcelable(TypeVerList verList, int attrId) {
		ArrayList<ITypeVer> values = new ArrayList<ITypeVer>();
		Iterator<TypeVer> it = verList.getValue().iterator();
		while (it.hasNext()) {
			values.add(TypeVer2parcelable(it.next(), attrId));
		}
		return new ITypeVerList(values);
	}

	private static IOID_Type OID_Type2parcelable(OID_Type type, int attrId) {
		return new IOID_Type(type.getValue().getValue(), AttributeUtils.attIdValue2string(attrId, type.getValue().getValue()));
	}

	private static IMetricIdList MetricIdList2parcelable(MetricIdList idList, int attrId) {
		ArrayList <IOID_Type> types = new ArrayList<IOID_Type>();
		for (OID_Type type: idList.getValue())
			types.add(OID_Type2parcelable(type, attrId));

		return new IMetricIdList(types);
	}

	private static ISFloatType SFloatType2parcelable(SFloatType value, int attrId) {
		return new ISFloatType(value.getExponent(), value.getMagnitude(), value.doubleValueRepresentation(),
							value.toString());
	}

	private static IOperationalState OperationalState2parcelable(OperationalState state, int attrId) {
		return new IOperationalState(state.getValue());
	}

	private static ISupplementalTypeList SupplementalTypeList2parcelable(SupplementalTypeList supTypeList, int attrId) {
		ArrayList<ITYPE> values = new ArrayList<ITYPE>();
		Iterator<TYPE> it = supTypeList.getValue().iterator();
		while (it.hasNext()) {
			values.add(TYPE2parcelable(it.next(), attrId));
		}
		return new ISupplementalTypeList(values);
	}

	private static IBasicNuObsValue BasicNuObsValue2parcelable(BasicNuObsValue obsValue, int attrId) {
		SFloatType value;
		try {
			value = new SFloatType(obsValue.getValue().getValue());
			return new IBasicNuObsValue(SFloatType2parcelable(value, attrId));
		} catch (Exception e) {
			return null;
		}
	}

	private static IMetricSpecSmall MetricSpecSmall2parcelable(MetricSpecSmall metricSpec, int attrId) {
		return new IMetricSpecSmall(BitString2parcelable(metricSpec.getValue(), attrId));
	}

	private static IInstNumber InstNumber2parcelable(InstNumber instNumber, int attrId) {
		return new IInstNumber(instNumber.getValue());
	}

	private static IPersonId PersonId2parcelable(PersonId personId, int attrId) {
		return new IPersonId(personId.getValue());
	}

	private static ISegmentStatistics SegmentStatistics2parcelable(
			SegmentStatistics segmentStatistics, int attrId){
		ArrayList<ISegmentStatisticEntry> values = new ArrayList<ISegmentStatisticEntry>();
		Iterator<SegmentStatisticEntry> it = segmentStatistics.getValue().iterator();
		while (it.hasNext()) {
			values.add(SegmentStatisticEntry2parcelable(it.next(), attrId));
		}
		return new ISegmentStatistics(values);
	}

	private static ISegmentStatisticEntry SegmentStatisticEntry2parcelable(SegmentStatisticEntry segmStatEntry, int attrId){
		return new ISegmentStatisticEntry(
				SegmStatType2parcelable(segmStatEntry.getSegm_stat_type(), attrId),
				OCTETSTRING2parcelable(segmStatEntry.getSegm_stat_entry(), attrId));
	}

	private static ISegmStatType SegmStatType2parcelable(SegmStatType segmStatType, int attrId){
		return new ISegmStatType(segmStatType.getValue());
	}

	private static ISegmEntryElem SegmEntryElem2parcelable(SegmEntryElem segmEntryElem, int attrId) {
		return new ISegmEntryElem(
				OID_Type2parcelable(segmEntryElem.getClass_id(), attrId),
				TYPE2parcelable(segmEntryElem.getMetric_type(), attrId),
				HANDLE2parcelable(segmEntryElem.getHandle(), attrId),
				AttrValMap2parcelable(segmEntryElem.getAttr_val_map(), attrId)
				);
	}

	private static ISegmEntryElemList SegmEntryElemList2parcelable(SegmEntryElemList segmEntryElemList, int attrId){
		ArrayList<ISegmEntryElem> values = new ArrayList<ISegmEntryElem>();
		Iterator<SegmEntryElem> it = segmEntryElemList.getValue().iterator();
		while (it.hasNext()) {
			values.add(SegmEntryElem2parcelable(it.next(), attrId));
		}
		return new ISegmEntryElemList(values);
	}

	private static ISegmEntryHeader SegmEntryHeader2parcelable(SegmEntryHeader segmEntryHeader, int attrId){
		return new ISegmEntryHeader(BitString2parcelable(segmEntryHeader.getValue(), attrId));
	}

	private static IPmSegmentEntryMap PmSegmentEntryMap2parcelable(PmSegmentEntryMap pmSegmentEntryMap, int attrId){
		return new IPmSegmentEntryMap(
				SegmEntryHeader2parcelable(pmSegmentEntryMap.getSegm_entry_header(), attrId),
				SegmEntryElemList2parcelable(pmSegmentEntryMap.getSegm_entry_elem_list(), attrId)
				);
	}

	public static final boolean getParcelableAttribute (Attribute asnAttr, IAttribute attr) {

		Parcelable parcel = null;

		if (attr == null)
			return false;

		if (asnAttr.getAttributeType() instanceof HANDLE)
			parcel = HANDLE2parcelable((HANDLE) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof TYPE)
			parcel = TYPE2parcelable((TYPE) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SystemModel)
			parcel = SystemModel2parcelable((SystemModel) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof byte[])
			parcel = OCTETSTRING2parcelable((byte []) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof ConfigId)
			parcel = ConfigId2parcelable((ConfigId) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof AttrValMap)
			parcel = AttrValMap2parcelable((AttrValMap) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof ProductionSpec)
			parcel = AttrProductionSpec2parcelable((ProductionSpec) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof MdsTimeInfo)
			parcel = MdsTimeInfo2parcelable((MdsTimeInfo) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof AbsoluteTime)
			parcel = AbsoluteTime2parcelable((AbsoluteTime) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof RelativeTime)
			parcel = RelativeTime2parcelable((RelativeTime) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof HighResRelativeTime)
			parcel = HighResRelativeTime2parcelable((HighResRelativeTime) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof AbsoluteTimeAdjust)
			parcel = AbsoluteTimeAdjust2parcelable((AbsoluteTimeAdjust) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof PowerStatus)
			parcel = PowerStatus2parcelable((PowerStatus) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof INT_U16)
			parcel = INT_U162parcelable((INT_U16) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof INT_U32)
			parcel = INT_U322parcelable((INT_U32) asnAttr.getAttributeType(),
					asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof BatMeasure)
			parcel = BatMeasure2parcelable((BatMeasure) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof RegCertDataList)
			parcel = RegCertDataList2parcelable((RegCertDataList) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof TypeVerList)
			parcel = TypeVerList2parcelable((TypeVerList) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof OID_Type)
			parcel = OID_Type2parcelable((OID_Type) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof BasicNuObsValue)
			parcel = BasicNuObsValue2parcelable((BasicNuObsValue) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof MetricSpecSmall)
			parcel = MetricSpecSmall2parcelable((MetricSpecSmall) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof MetricIdList)
			parcel = MetricIdList2parcelable((MetricIdList) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof OperationalState)
			parcel = OperationalState2parcelable((OperationalState) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SupplementalTypeList)
			parcel = SupplementalTypeList2parcelable((SupplementalTypeList) asnAttr.getAttributeType(),
							asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof StoSampleAlg)
			parcel = StoSampleAlg2parcelable((StoSampleAlg) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof PmStoreCapab)
			parcel = PmStoreCapab2parcelable((PmStoreCapab) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof InstNumber)
			parcel = InstNumber2parcelable((InstNumber) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof PersonId)
			parcel = PersonId2parcelable((PersonId) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmentStatistics)
			parcel = SegmentStatistics2parcelable((SegmentStatistics) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmentStatisticEntry)
			parcel = SegmentStatisticEntry2parcelable((SegmentStatisticEntry) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmStatType)
			parcel = SegmStatType2parcelable((SegmStatType) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof PmSegmentEntryMap)
			parcel = PmSegmentEntryMap2parcelable((PmSegmentEntryMap) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmEntryHeader)
			parcel = SegmEntryHeader2parcelable((SegmEntryHeader) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmEntryElemList)
			parcel = SegmEntryElemList2parcelable((SegmEntryElemList) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());
		else if (asnAttr.getAttributeType() instanceof SegmEntryElem)
			parcel = SegmEntryElem2parcelable((SegmEntryElem) asnAttr
					.getAttributeType(), asnAttr.getAttributeID());

		if (parcel != null) {
			attr.setAttr(parcel);
			attr.setAttrId(asnAttr.getAttributeID());
			attr.setAttrIdStr(asnAttr.getAttributeName());
			return true;
		}

		Logging.error("Unknown method provided. Can't create parcelable attribute for type " + asnAttr.getAttributeType().getClass());
		return false;
	}

}
