package com.pku.wireless;

import java.util.EventObject;

public class MeasureEvent extends EventObject{
	private Object measure;

	public MeasureEvent(Object source, Object measure) {
		super(source);
		this.measure = measure;
	}
	
	public Object getMeasures(){
		return this.measure;		
	}
}
