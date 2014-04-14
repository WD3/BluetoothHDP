/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package es.libresoft.openhealth.events;

//import cmdTester.ShellMeasureReporter;
//import es.libresoft.openhealth.android.AndroidMeasureReporter;

public class MeasureReporterFactory {

	//private static int defaultReporter = 0; //not measure reporter set
	private static MeasureReporter defaultReporter = null;
	
	//Supported platforms
	public static final int ANDROID = 1;
	public static final int SHELL = 2;

	/**
	 * Set the default measure reporter of the entire system
	 * @param platform target platform
	 */
	public static final void setDefaultMeasureReporter (MeasureReporter mr) {
		defaultReporter = mr;
	}

	/**
	 * Get default measure reporter established for the target platform
	 * @return the target measure reporter for the specific platform
	 * @throws Exception if the target measure reporter is not set
	 */
	public static final MeasureReporter getDefaultMeasureReporter () throws Exception {
		if (defaultReporter==null)
			throw new Exception("Measure reporter is not setted to any target platform");
		//return getMeasureReporterFor(defaultReporter);
		return defaultReporter;
	}

	/**
	 * Get measure reporter class to send measure events from manager to application layer
	 * @param platform
	 * @return
	 */
	/*public static final MeasureReporter getMeasureReporterFor(int platform){
		switch (platform) {
		//case ANDROID : return new AndroidMeasureReporter();
		case SHELL : return new ShellMeasureReporter();
		default : return null;
		}
	}*/
}
