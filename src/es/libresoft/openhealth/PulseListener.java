package es.libresoft.openhealth;

import java.util.EventListener;

public interface PulseListener extends EventListener{
	
	public void getMeasure(PulseEvent event);
}

