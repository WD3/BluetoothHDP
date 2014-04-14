package cmdTester;

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

import Config.BloodPressureAgent;
import Config.BloodPressureMeasure;
import Config.PulseMeasure;
import es.libresoft.openhealth.BloodEvent;
import es.libresoft.openhealth.BloodListener;
import es.libresoft.openhealth.PulseEvent;
import es.libresoft.openhealth.PulseListener;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.Logging;

public class MeasureDataDecoder{
	private List<Object> measures;
	private List<Object> attributes;
	private Iterator<Object> ims;
	private Iterator<Object> iat;
	private BloodPressureMeasure bloodPressureMeasure;
	private PulseMeasure pulseMeasure;
	static Collection bListeners;
	static Collection pListeners;
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
	public static void setBloodListener(BloodListener bListener) {
		if (bListeners == null) {
			bListeners = new HashSet();
		}
		bListeners.add(bListener);
	}
	public static void removeBloodListener(BloodListener bListener) {
		if (bListeners == null)
			return;
		bListeners.remove(bListener);
	}
	public static void setPulseListener(PulseListener pListener) {
		if (pListeners == null) {
			pListeners = new HashSet();
		}
		pListeners.add(pListener);
	}
	public static void removePulseListener(PulseListener pListener) {
		if (pListeners == null)
			return;
		pListeners.remove(pListener);
	}
	public void receiveMeasures() {
		if (!attributes.isEmpty()) {
			Logging.debug("测量的属性为: ");
			while (iat.hasNext()) {
				Attribute attrib = (Attribute) iat.next();
				receiveAttribute(attrib);
			}		
			if(bloodPressureFlag){
				Iterator iter = bListeners.iterator();
				while (iter.hasNext()) {
					BloodListener bListener = (BloodListener) iter.next();
					bListener.getMeasure(new BloodEvent(this,bloodPressureMeasure));
				}
				bloodPressureFlag = false;
			}
			else if(pulseFlag){
				Iterator iter = pListeners.iterator();
				while (iter.hasNext()) {
					PulseListener pListener = (PulseListener) iter.next();
					pListener.getMeasure(new PulseEvent(this,pulseMeasure));
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
				bloodPressureMeasure = new BloodPressureMeasure();
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
