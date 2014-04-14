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
package es.libresoft.mdnf;

import java.lang.Math;

import es.libresoft.openhealth.logging.Logging;

public class FloatType {

	public static final int MINEXP = -128;				//-1*(2^7)
	public static final int MAXEXP = 127;				// 2^7-1
	public static final int MINMAG = -16777210; 		// -2*((2^23)-3)
	public static final int MAXMAG = 16777210; 			// 2*((2^23)-3)
	public static final int NaN = 8388607; 				// ((2^23)-1)
	public static final int NRes = -8388608;			// -(2^23)
	public static final int INFINITY_NEG = -8388606;	// -1*((2^23)-2)
	public static final int INFINITY_POS= 8388606;		// (2^23)-2
	public static final int RESERVED = -8388607;		//-1*((2^23)-1)

	private int exponent; // 8 bit signed
	private int magnitude; // 24 bit signed

	public FloatType (int exponent,int magnitude) throws Exception {
		if (isMDNFSpecialValue(magnitude))
			this.exponent = 0;
		else {
			checkConstraints (exponent, magnitude);
			this.exponent = exponent;
		}
		this.magnitude = magnitude;
	}

	public FloatType (long rawFloatType) throws Exception{
		if (rawFloatType>4294967295L)
			throw new Exception("Range is not valid");
		int fourLSB = (int)(rawFloatType & 0x00000000FFFFFFFF);
		convertInt2FloatType(fourLSB);
	}

	public FloatType (int rawFloatType) throws Exception{
		convertInt2FloatType(rawFloatType);
	}

	private void convertInt2FloatType(int rawFloatType) throws Exception{
		int exponent = rawFloatType >> 24;

		int magnitude = (rawFloatType & 0x00FFFFFF);

		if ((magnitude & 0x00800000)!=0) {
			magnitude = magnitude - 16777216;
		}
		if (isMDNFSpecialValue(magnitude))
			this.exponent = 0;
		else {
			checkConstraints (exponent, magnitude);
			this.exponent = exponent;
		}
		this.magnitude = magnitude;
	}

	public int getExponent() {return this.exponent;}

	public int getMagnitude() {return this.magnitude;}

	public long longValue() {
		long v =  getRawRepresentation();
		v = (v & 0x00000000FFFFFFFFL);; //Get only four less symbolic bytes
		return v;
	}

	public double doubleValueRepresentation (){
		Logging.debug("exponente: " + this.exponent + ", calc: " + Math.pow(10, this.exponent));
		if (isMDNFSpecialValue(this.magnitude))
			return this.magnitude;
		else return magnitude*(Math.pow(10, exponent));
	}

	public String toString () {
		switch (this.magnitude) {
			case NaN: return "NaN";
			case NRes: return "NRes";
			case INFINITY_NEG: return "-INFINITY";
			case INFINITY_POS: return "+INFINITY";
			case RESERVED : return "Reserved for future use";
			default: return this.magnitude + "*10^" + this.exponent;
		}
	}

	private boolean isMDNFSpecialValue(int magnitude){
		switch (magnitude) {
			case NaN: return true;
			case NRes: return true;
			case INFINITY_NEG: return true;
			case INFINITY_POS: return true;
			case RESERVED : return true;
			default: return false;
		}
	}

	private void checkConstraints (int exponent,int magnitude) throws Exception{
		if ((exponent > MAXEXP) || (exponent < MINEXP))
			throw new Exception ("Exponent is not in valid Float-Type range");
		if ((magnitude > MAXMAG) || (magnitude < MINMAG))
			throw new Exception ("Magnitude is not in valid Float-Type range");
	}

	public int getRawRepresentation(){
		int raw = this.exponent << 24;
		raw = raw | (magnitude & 0x00FFFFFF);
		return raw;
	}
}
