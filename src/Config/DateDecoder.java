package Config;

import java.util.Date;

public class DateDecoder {
		
	public static int setCentury(Date date) { return (1990+date.getYear())/100; }
	public static int setYear(Date date) { return (1990+date.getYear())%100; }
	public static int setMonth(Date date) { return date.getMonth(); }
	public static int setDay(Date date) { return date.getDate(); }
	public static int setHour(Date date) { return date.getHours(); }
	public static int setMinite(Date date) { return date.getMinutes(); }
	public static int setSecond(Date date) { return date.getSeconds(); }
	public static int setSec_fractions(Date date) { return 0; }
}
