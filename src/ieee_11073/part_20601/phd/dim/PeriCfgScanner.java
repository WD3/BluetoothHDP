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
package ieee_11073.part_20601.phd.dim;

import java.util.Hashtable;

import ieee_11073.part_10101.Nomenclature;

	/**
	 * The PeriCfgScanner class represents a class that can be instantiated. PeriCfgScanner
	 * objects are used to send reports containing Periodic data, that is, data sampled during
	 * fixed periods. It buffers any data value changes to be sent as part of a periodic report.
	 * Event reports shall be sent with a time interval equal to the Reporting-Interval
	 * attribute value. The number of observations for each metric object is dependent on the
	 * metric object’s update interval and the scanner’s Reporting-Interval.
	 * Example: A periodic configurable scanner is set up to “scan” two metric objects with a
	 * Reporting-Interval of 1 s. The two objects update their corresponding observed value
	 * periodically with an interval of 1 s and 1⁄2 s, respectively. The periodic configurable
	 * scanner then issues event reports every second containing one observation scan of metric
	 * object #1 and two observation scans of metric object #2.
	 */

public abstract class PeriCfgScanner extends CfgScanner implements PeriCfgScanner_Events {

	public PeriCfgScanner(Hashtable<Integer,Attribute> attributeList) throws InvalidAttributeException {
		super(attributeList);
	}

	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_SCAN_CFG_PERI;
	}
}
