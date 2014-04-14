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

public class SFloatType {

	public static final int MINEXP = -8;			//-1*(2^3)
	public static final int MAXEXP = 7;				// 2^3-1
	public static final int MINMAG = -4090; 		// -2*((2^11)-3)
	public static final int MAXMAG = 4090; 			// 2*((2^11)-3)
	public static final int NaN = 2047; 			// ((2^11)-1)
	public static final int NRes = -2048;			// -1*(2^11)
	public static final int INFINITY_NEG = -2046;	// -1*((2^11)-2)
	public static final int INFINITY_POS= 2046;		// (2^11)-2
	public static final int RESERVED = -2047;		//-1*((2^11)-1)

	private short exponent; // 4 bit signed
	private short magnitude; // 12 bit signed

	public SFloatType (short exponent,short magnitude) throws Exception {
		if (isMDNFSpecialValue(magnitude))
			this.exponent = 0;
		else {
			checkConstraints (exponent, magnitude);
			this.exponent = exponent;
		}
		this.magnitude = magnitude;
	}

	public SFloatType (int rawSFloatType) throws Exception{
		if (rawSFloatType>65535)
			throw new Exception("Range is not valid");
		short twoLSB = (short)(rawSFloatType & 0xFFFF);
		convertShort2SFloatType(twoLSB);
	}

	public SFloatType (short rawSFloatType) throws Exception{
		convertShort2SFloatType(rawSFloatType);
	}

	private void convertShort2SFloatType(short rawSFloatType) throws Exception{
		short exponent = (short) (rawSFloatType >> 12);

		short magnitude = (short) (rawSFloatType & 0x0FFF);

		if ((magnitude & 0x0800)!=0) {
			magnitude = (short) (magnitude - 4096);
		}
		if (isMDNFSpecialValue(magnitude))
			this.exponent = 0;
		else {
			checkConstraints (exponent, magnitude);
			this.exponent = exponent;
		}
		this.magnitude = magnitude;
	}

	public short getExponent() {return this.exponent;}

	public short getMagnitude() {return this.magnitude;}

	public int intValue() {
		int v =  getRawRepresentation();
		v = (v & 0x0000FFFF); //Get only four less symbolic bytes
		return v;
	}

	public long longValue() {
		long v =  getRawRepresentation();
		v = (v & 0x000000000000FFFFL); //Get only four less symbolic bytes
		return v;
	}

	public double doubleValueRepresentation (){
		if (isMDNFSpecialValue(this.magnitude))
			return this.magnitude;
		else return this.magnitude*(Math.pow(10, this.exponent));
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

	public short getRawRepresentation(){
		short raw = (short) (this.exponent << 12);
		raw = (short) (raw | (magnitude & 0x0FFF));
		return raw;
	}
}
