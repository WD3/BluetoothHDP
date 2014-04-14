package es.libresoft.openhealth.android;

import es.libresoft.openhealth.logging.ILogging;

public class AndroidILogging implements ILogging{
	@Override
	public void error(String str) {
		System.err.println(str);
	}

	@Override
	public void debug(String str) {
		System.out.println(str);
	}

	@Override
	public void info(String str) {
		System.out.println(str);
	}
}
