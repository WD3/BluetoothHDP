/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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

package es.libresoft.openhealth.logging;


public class Logging {

	private static ILogging log = new ILogging() {

		@Override
		public void error(String str) {
			/* Do nothing by default */
		}

		@Override
		public void debug(String str) {
			/* Do nothing by default */
		}

		@Override
		public void info(String str) {
			/* Do nothing by default */
		}
	};

	/**
	 * Set the default logging mechanism of the entire system
	 *
	 * @param log target platform
	 */
	public static final void setDefaultLogGenerator (ILogging log) {
		Logging.log = log;
	}

	public static void error(String str) {
		log.error(str);
	}

	public static void debug(String str) {
		log.debug(str);
	}

	public static void info(String str) {
		log.info(str);
	}
}
