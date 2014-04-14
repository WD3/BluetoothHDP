package es.libresoft.openhealth.android;

import java.util.Date;
import java.util.List;

import es.libresoft.openhealth.android.aidl.types.measures.IMeasureArray;
import es.libresoft.openhealth.android.aidl.types.measures.IValueMeasure;


public class IBloodPressureMeasure {
	private int highPressure;
	private int lowPressure;
	private int average;
	private String unit;
	private Date date;
	private String type = "Nonivasive blood pressure";
	
	public String getType(){
		return this.type;
	}
	public void setHPressure(List list){
		this.highPressure = (int)(((IValueMeasure) list.get(0)).getFloatType());
	}
	public int getHPressure(){
		return this.highPressure;
	}
	
	public void setLPressure(List list){
		this.lowPressure = (int)(((IValueMeasure) list.get(1)).getFloatType());
	}
	public int getLPressure(){
		return this.lowPressure;
	}	
	
	public void setAPressure(List list){
		this.average = (int)(((IValueMeasure) list.get(2)).getFloatType());
	}
	public int getAPressure(){
		return this.average;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	public Date getDate(){
		return this.date;
	}
	
	public void setUint(String unit){
		this.unit = unit;
	}
	public String getUnit(){
		return this.unit;
	}

}
