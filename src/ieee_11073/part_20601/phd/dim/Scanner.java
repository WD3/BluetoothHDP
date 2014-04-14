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
	 * A scanner object is an observer and “summarizer” of object attribute values. It observes
	 * attributes of metric objects (e.g., numeric objects) and generates summaries in the form
	 * of notification event reports.
	 * The scanner class is an abstract class defining attributes, methods, events, and services
	 * that are common for its subclasses. As such, it cannot be instantiated.
	 * The scanner concept provides three different event report notifications: variable format,
	 * fixed format, and grouped format. See 7.4.5 for the reporting of observed object attributes.
	 * The event report formats are described further in 6.3.9.4.5, 6.3.9.5.5, and A.11.5,
	 * respectively.
	 * More specialized scanner classes are derived from the scanner base class.
	 */
public abstract class Scanner extends DIM implements SET_Service {
	public Scanner (Hashtable<Integer,Attribute> attributeList) throws InvalidAttributeException{
		super(attributeList);
	}

	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_SCAN;
	}
}
