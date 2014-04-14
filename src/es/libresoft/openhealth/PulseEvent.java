package es.libresoft.openhealth;

import java.util.EventObject;

public class PulseEvent extends EventObject {
	private Object measure;

	public PulseEvent(Object source, Object measure) {
		super(source);
		// TODO Auto-generated constructor stub
		this.measure = measure;
	}

	public Object getMeasures() {
		return measure;

	}

}
