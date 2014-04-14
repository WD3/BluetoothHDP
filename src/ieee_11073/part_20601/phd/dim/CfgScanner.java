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
	 * The CfgScanner class is an abstract class defining attributes, methods, events,
	 * and services that are common for its subclasses. In particular, it defines the
	 * communication behavior of a configurable scanner object. As such, it cannot be
	 * instantiated.
	 */

public abstract class CfgScanner extends Scanner {

	public CfgScanner(Hashtable<Integer,Attribute> attributeList) throws InvalidAttributeException {
		super(attributeList);
	}

	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_SCAN_CFG;
	}
}
