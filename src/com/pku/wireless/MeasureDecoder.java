package com.pku.wireless;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cmdTester.ShellMeasure;

import Config.BloodPressureAgent;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.Logging;

public class MeasureDecoder{
	private List<Object> measures;
	private List<Object> attributes;
	private Iterator<Object> ims;
	private Iterator<Object> iat;
	private BloodMeasure bloodPressureMeasure;
	private PulseMeasure pulseMeasure;
	static Collection mListeners;
	private MeasureReporter mr;
	
	private boolean bloodPressureFlag;
	private boolean pulseFlag;
	
	public void setMeasureReporter(MeasureReporter mr){
		this.mr = mr;
	}
	public void init(){
		measures = mr.getMeasures();
		ims = measures.iterator();
		attributes = mr.getAttributes();
		iat = attributes.iterator();
	}
	public static void setMeasureListener(MeasureListener mListener) {
		if (mListeners == null) {
			mListeners = new HashSet();
		}
		mListeners.add(mListener);
	}
	public static void removeMeasureListener(MeasureListener mListener) {
		if (mListeners == null)
			return;
		mListeners.remove(mListener);
	}
	public void receiveMeasures() {
		if (!attributes.isEmpty()) {
			Logging.debug("测量的属性为: ");
			while (iat.hasNext()) {
				Attribute attrib = (Attribute) iat.next();
				receiveAttribute(attrib);
			}		
			if(bloodPressureFlag){
				Iterator iter = mListeners.iterator();
				while (iter.hasNext()) {
					MeasureListener mListener = (MeasureListener) iter.next();
					mListener.getBloodMeasure(new MeasureEvent(this,bloodPressureMeasure));
				}
				bloodPressureFlag = false;
			}
			else if(pulseFlag){
				Iterator iter = mListeners.iterator();
				while (iter.hasNext()) {
					MeasureListener mListener = (MeasureListener) iter.next();
					mListener.getPulseMeasure(new MeasureEvent(this,pulseMeasure));
				}
				pulseFlag = false;
			}
		}
	}
	

	public void receiveAttribute(Attribute attrib) {
		switch(attrib.getAttributeID()){
		case Nomenclature.MDC_ATTR_ID_TYPE:
			decodeType((TYPE)attrib.getAttributeType());
			break;
		case Nomenclature.MDC_ATTR_UNIT_CODE:
			decodeUnitCode((OID_Type)attrib.getAttributeType());			
			break;
		case Nomenclature.MDC_ATTR_ID_HANDLE:
			break;
		case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST:
			break;
		default:
			break;
		}
	}

	public void receiveMeasure(ShellMeasure measure) {
		switch (measure.getType()){
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC:
			if(bloodPressureFlag){
				bloodPressureMeasure.setHPressure(measure.getData());
				bloodPressureMeasure.setLPressure(measure.getData());
				bloodPressureMeasure.setAPressure(measure.getData());
			}				
			Logging.debug("数值:"+measure.getData());
			break;
		case Nomenclature.MDC_ATTR_TIME_STAMP_ABS:
			if(bloodPressureFlag)
				bloodPressureMeasure.setDate(decodeAbsoluteTime((AbsoluteTime)measure.getData()));				
			Logging.debug("时间:"+decodeAbsoluteTime((AbsoluteTime)measure.getData()).toString());
			break;
		default:
			break;
		}
	}

	public void decodeUnitCode(OID_Type unit) {
		switch (unit.getValue().getValue()) {
		case BloodPressureAgent.MDC_DIM_MMHG:
			if(bloodPressureFlag)
				bloodPressureMeasure.setUint("mmHg");
			break;
		case BloodPressureAgent.MDC_DIM_BEAT_PER_MIN:
			if(pulseFlag)
				pulseMeasure.setUint("搏/分");
			break;
		default:
			Logging.debug("默认单位:" + unit.getValue().getValue());
			break;
		}
	}

	public void decodeType(TYPE type) {
		switch (type.getCode().getValue().getValue()) {
			case BloodPressureAgent.MDC_PRESS_BLD_NONINV:
				System.out.println("Nonivasive blood pressure");
				bloodPressureMeasure = new BloodMeasure();
				bloodPressureFlag = true;
				while (ims.hasNext()) {
					ShellMeasure measure = (ShellMeasure) ims.next();
					receiveMeasure(measure);
				}
				break;
			case BloodPressureAgent.MDC_PULS_RATE_NON_INV:
				System.out.println("Pulse");
				pulseMeasure = new PulseMeasure();
				pulseFlag = true;
				while (ims.hasNext()) {
					ShellMeasure measure = (ShellMeasure) ims.next();
					receiveMeasure(measure);
				}
				break;
			default:
				Logging.debug("默认类型:" + type.getCode().getValue().getValue());
				break;
		}
	}

	public Date decodeAbsoluteTime(AbsoluteTime time) {
		int century = time.getCentury().getValue();
		int year = time.getYear().getValue();
		int month = time.getMonth().getValue();
		int day = time.getDay().getValue();
		int hour = time.getHour().getValue();
		int min = time.getMinute().getValue();
		int second = time.getSecond().getValue();
		String timeString = century
				+ year + "-" + month
				+ "-" + day + " "
				+ hour + ":" + min
				+ ":" + second;
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date datetime = null;
		try {
			datetime = formatDate.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetime;
	}
}
