package es.libresoft.openhealth;

import java.util.EventObject;

public class BloodEvent extends EventObject {
	private Object measure;

	public BloodEvent(Object source, Object measure) {
		super(source);
		this.measure = measure;
	}
	
	public Object getMeasures(){
		return this.measure;		
	}

}
