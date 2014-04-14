package Config;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.AttributeList;
import ieee_11073.part_20601.asn1.AuthBody;
import ieee_11073.part_20601.asn1.AuthBodyAndStrucType;
import ieee_11073.part_20601.asn1.AuthBodyStrucType;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.BasicNuObsValueCmp;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.INT_U32;
import ieee_11073.part_20601.asn1.INT_U8;
import ieee_11073.part_20601.asn1.MdsTimeCapState;
import ieee_11073.part_20601.asn1.MdsTimeInfo;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.NomPartition;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.RegCertData;
import ieee_11073.part_20601.asn1.RegCertDataList;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.SFLOAT_Type;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.asn1.TimeProtocolId;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.bn.types.BitString;

import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.DeviceConfigCreator;
import es.libresoft.openhealth.Specialization;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;



public class BloodPressureAgent extends Specialization{
	
	public static final int MDC_PRESS_BLD_NONINV = 18948;
	public static final int MDC_PULS_RATE_NON_INV = 18474;
	
	public static final int MDC_DIM_BEAT_PER_MIN = 2720;
	public static final int MDC_DIM_MMHG = 3872;
	
	public BloodPressureAgent(){
		dev_conf = generateTestDeviceConfig();
		configdata = new ArrayList<ConfigObject>();
		configdata.add(generateNumericConfig1());
		configdata.add(generateMDSConfig());
		Measures = new ArrayList<Map<HANDLE, Object>>();
	}
	public void addMeasure(int high, int low, int avg, Date date){
		Measures.add(generateMeasure(high, low, avg, date));
	}
	
	private DeviceConfig generateTestDeviceConfig()
	{
		DeviceConfigCreator dev_conf = new DeviceConfigCreator();
		dev_conf.setDataProtoId(AgentConfig.data_proto_id);
		
		dev_conf.setPhdId(AgentConfig.dev_config_id);
		dev_conf.setProtocolVersion(AgentConfig.protocol_version);
		dev_conf.setEncondigRules(AgentConfig.enc_rules);		
		dev_conf.setNomenclatureVersion(AgentConfig.nomenclature_version);
		
		dev_conf.setSystemId(AgentConfig.system_id);
		dev_conf.setSystemType(AgentConfig.syste_type);
		dev_conf.setAssocVersion(AgentConfig.assoc_version);
		dev_conf.setFunctionalUnits(AgentConfig.functional_units);

		/*Data req mode flags*/
		dev_conf.setDataReqModeFlags(new byte[]{(byte)0xFF,(byte)0xFF});
		dev_conf.setDataReqInitAgentCount(1);
		// Maximum number of parallel manager initiated data requests:
		dev_conf.setDataReqInitManagerCount(1);
		
		return dev_conf.getDeviceConfig();
	}


	private ConfigObject generateNumericConfig1(){
		Hashtable<Integer,Attribute> attribs = new Hashtable<Integer, Attribute>();
		Attribute attrib;
		ConfigObject confObj = new ConfigObject();
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(1));
		confObj.setObj_handle(handle);
		OID_Type oid= new OID_Type();
		oid.setValue(new INT_U16(Nomenclature.MDC_MOC_VMO_METRIC_NU));
		confObj.setObj_class(oid);
		try {
			//set Handle
			attrib = new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE, confObj.getObj_handle());
			attribs.put(Nomenclature.MDC_ATTR_ID_HANDLE, attrib);
			//set Unit Code		
			oid= new OID_Type();
			oid.setValue(new INT_U16(MDC_DIM_MMHG));
			attrib = new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE, oid);
			attribs.put(Nomenclature.MDC_ATTR_UNIT_CODE, attrib);
			//set type
			TYPE t =  new TYPE();
			oid= new OID_Type();
			oid.setValue(new INT_U16(MDC_PRESS_BLD_NONINV));
			t.setCode(oid);
			t.setPartition(new NomPartition(2));;
			attrib = new Attribute(Nomenclature.MDC_ATTR_ID_TYPE, t);
			attribs.put(Nomenclature.MDC_ATTR_ID_TYPE, attrib);
			//set attribute-value-map
			AttrValMap avm = new AttrValMap(new ArrayList<AttrValMapEntry>());
			AttrValMapEntry entry = new AttrValMapEntry();
			oid= new OID_Type();
			oid.setValue(new INT_U16(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC));
			entry.setAttribute_id(oid);
			entry.setAttribute_len(10);
			avm.add(entry);
			entry = new AttrValMapEntry();
			oid= new OID_Type();
			oid.setValue(new INT_U16(Nomenclature.MDC_ATTR_TIME_STAMP_ABS));
			entry.setAttribute_id(oid);
			entry.setAttribute_len(8);
			avm.add(entry);
			attrib = new Attribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, avm);
			attribs.put(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, attrib);
			//set metric_spec_small 
			MetricSpecSmall mss = new MetricSpecSmall();
			mss.setValue(new BitString(new byte[]{(byte)0x20,(byte)0x00}));
			attrib = new Attribute(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, mss);
			attribs.put(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, attrib);
		} catch (InvalidAttributeException e) {
			e.printStackTrace();
		}
		try {
			confObj.setAttributes(getAttributeList(attribs,"MDER"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return confObj;
	}

	private Map<HANDLE,Object> generateMeasure(int high, int low, int avg, Date date){
		Map<HANDLE,Object> measure = new Hashtable<HANDLE, Object>();
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(1));
		ArrayList<Object> objlist = new ArrayList<Object>();
		
		//set measure value
		Collection<BasicNuObsValue> bnvlist = new ArrayList<BasicNuObsValue>();	
		BasicNuObsValue bnv = new BasicNuObsValue();
		bnv.setValue(new SFLOAT_Type(high));
		bnvlist.add(bnv);	
		bnv = new BasicNuObsValue();
		bnv.setValue(new SFLOAT_Type(low));
		bnvlist.add(bnv);
		bnv = new BasicNuObsValue();
		bnv.setValue(new SFLOAT_Type(avg));
		bnvlist.add(bnv);
		BasicNuObsValueCmp cmp_val = new BasicNuObsValueCmp(bnvlist);
		//set measure date
		AbsoluteTime time = new AbsoluteTime();
		time.setCentury(new INT_U8(DateDecoder.setCentury(date)));
		time.setYear(new INT_U8(DateDecoder.setYear(date)));
		time.setMonth(new INT_U8(DateDecoder.setMonth(date)));
		time.setDay(new INT_U8(DateDecoder.setDay(date)));
		time.setHour(new INT_U8(DateDecoder.setHour(date)));
		time.setMinute(new INT_U8(DateDecoder.setMinite(date)));
		time.setSecond(new INT_U8(DateDecoder.setSecond(date)));
		time.setSec_fractions(new INT_U8(DateDecoder.setSec_fractions(date)));
		objlist.add(cmp_val);
		objlist.add(time);
		measure.put(handle, objlist);
		return measure;
	}
	
	
	private ConfigObject generateMDSConfig(){
		Hashtable<Integer,Attribute> attribs  = new Hashtable<Integer, Attribute>();
		Attribute attrib;
		ConfigObject confObj = new ConfigObject();
		HANDLE handle = new HANDLE();
		handle.setValue(new INT_U16(0));
		confObj.setObj_handle(handle);
		OID_Type oid= new OID_Type();
		oid.setValue(new INT_U16(Nomenclature.MDC_MOC_VMS_MDS_SIMP));
		confObj.setObj_class(oid);
		try {
			//set Handle
			attrib = new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE, confObj.getObj_handle());
			attribs.put(Nomenclature.MDC_ATTR_ID_HANDLE, attrib);
			//set Mds-Time_info		
			MdsTimeInfo timeInfo = new MdsTimeInfo();
			MdsTimeCapState mtc = new MdsTimeCapState();
			mtc.setValue(new BitString(new byte[]{(byte) 0xC0,(byte) 0x80}));
			timeInfo.setMds_time_cap_state(mtc);
			TimeProtocolId tpid = new TimeProtocolId();
			oid = new OID_Type();
			oid.setValue(new INT_U16(Nomenclature.MDC_TIME_SYNC_NONE));//no time synchronization protocol supported
			tpid.setValue(oid);
			timeInfo.setTime_sync_protocol(tpid);
			RelativeTime rt = new RelativeTime();
			rt.setValue(new INT_U32(0xFFFFFFFFL));//0xFFFFFFFF
			timeInfo.setTime_sync_accuracy(rt);
			timeInfo.setTime_resolution_abs_time(100);
			timeInfo.setTime_resolution_high_res_time(new INT_U32(0l));
			timeInfo.setTime_resolution_rel_time(0);
			attrib = new Attribute(Nomenclature.MDC_ATTR_MDS_TIME_INFO, timeInfo);
			attribs.put(Nomenclature.MDC_ATTR_MDS_TIME_INFO, attrib);
			//set Confirm-Timeout
			rt = new RelativeTime();
			rt.setValue(new INT_U32(10l));
			attrib = new Attribute(Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT, rt);
			attribs.put(Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT, attrib);
			//set attribute-value-map
			AttrValMap avm = new AttrValMap(new ArrayList<AttrValMapEntry>());
			AttrValMapEntry entry = new AttrValMapEntry();
			oid= new OID_Type();
			oid.setValue(new INT_U16(Nomenclature.MDC_ATTR_TIME_ABS_ADJUST));
			entry.setAttribute_id(oid);
			entry.setAttribute_len(6);
			avm.add(entry);
			attrib = new Attribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, avm);
			attribs.put(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, attrib);
			//set Reg-Cert-Data-List
			RegCertDataList regList = new RegCertDataList();
			Collection<RegCertData> regCollection = new ArrayList<RegCertData>();
			RegCertData reg = new RegCertData();
			AuthBodyAndStrucType abast =new AuthBodyAndStrucType();
			abast.setAuth_body(new AuthBody(2));
			abast.setAuth_body_struc_type(new AuthBodyStrucType(1));
			reg.setAuth_body_and_struc_type(abast);
			reg.setAuth_body_data(new byte[]{});
			regCollection.add(reg);
			regList.setValue(regCollection);
			attrib = new Attribute(Nomenclature.MDC_ATTR_REG_CERT_DATA_LIST, regList);
			attribs.put(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, attrib);
		} catch (InvalidAttributeException e) {
			e.printStackTrace();
		}
		try {
			confObj.setAttributes(getAttributeList(attribs,"MDER"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return confObj;
	}
}
