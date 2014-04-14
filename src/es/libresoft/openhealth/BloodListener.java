package es.libresoft.openhealth;

import java.util.EventListener;

public interface BloodListener extends EventListener{
	
	public void getMeasure(BloodEvent event);
}
