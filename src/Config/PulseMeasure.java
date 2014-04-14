package Config;

import java.util.Date;

public class PulseMeasure {
	private int pulse;
	private Date date;
	private String unit;
	
	public void setPulse(double pulse){
		this.pulse = (int)pulse;
	}
	public int getPulse(){
		return this.pulse;
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
