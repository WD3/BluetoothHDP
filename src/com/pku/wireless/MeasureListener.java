package com.pku.wireless;

import java.util.EventListener;


public interface MeasureListener extends EventListener{
	
	public void getBloodMeasure(MeasureEvent event);
	public void getPulseMeasure(MeasureEvent event);
	
}
