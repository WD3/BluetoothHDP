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
import java.util.Iterator;

import es.libresoft.openhealth.logging.Logging;

import ieee_11073.part_10101.Nomenclature;

	/**
	 * An instance of the PM-segment class represents a persistently stored episode of measurement
	 * data. A PM-segment object is not part of the static agent configuration because the number of
	 * instantiated PM-segment instances may dynamically change. The manager accesses PM-segment
	 * objects indirectly by methods and events of the PM-store object.
	 */
public class PM_Segment extends DIM {

	private static int[] mandatoryIds = {Nomenclature.MDC_ATTR_ID_INSTNO,
										Nomenclature.MDC_ATTR_PM_SEG_MAP,
										Nomenclature.MDC_ATTR_OP_STAT,
										//Nomenclature.MDC_ATTR_SEG_FIXED_DATA,
										Nomenclature.MDC_ATTR_TRANSFER_TIMEOUT,
										};

	public PM_Segment(Hashtable<Integer,Attribute> attributeList) throws InvalidAttributeException {
		super(attributeList);
		printValues();
	}

	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_PM_SEGMENT;
	}

	private void printValues() {
		Logging.debug("Printing attributes PM_SEGMENT");

		Iterator<Integer> attrs = attributeList.keySet().iterator();
		while (attrs.hasNext()) {
			Integer attrId = attrs.next();
			Attribute attr =  attributeList.get(attrId);
			if (attr == null)
				continue;

			if (attr.getAttributeID() == Nomenclature.MDC_ATTR_PM_SEG_LABEL_STRING) {
				byte[] octet = (byte[]) attr.getAttributeType();
				String sysId = new String(octet);
				Logging.debug("Label String: " + sysId);
			}
		}
	}
	@Override
	protected void checkAttributes(
			Hashtable<Integer, Attribute> attributes)
			throws InvalidAttributeException {
		/* Check mandatory attributes of the PM_Segments object */
		for (int i=0; i<mandatoryIds.length; i++){
			if (!attributes.containsKey(mandatoryIds[i]))
				throw new InvalidAttributeException("Attribute id " + mandatoryIds[i] + " is not assigned in PM_Store Object.");
		}
	}
}
