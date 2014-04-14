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

package es.libresoft.openhealth.android.aidl.types;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class IBITSTRING implements Parcelable {
	private byte[] bits;
	private int trailBits = 0;

	public static final Parcelable.Creator<IBITSTRING> CREATOR =
			new Parcelable.Creator<IBITSTRING>() {
		public IBITSTRING createFromParcel(Parcel in) {
			return new IBITSTRING(in);
		}

		public IBITSTRING[] newArray(int size) {
			return new IBITSTRING[size];
		}
	};

	public IBITSTRING () {

	}

	public IBITSTRING (byte[] bits, int trailBits) {
		this.bits = bits;
		this.trailBits = trailBits;
	}

	private IBITSTRING (Parcel in) {
		bits = in.createByteArray();;
		trailBits = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByteArray(bits);
		dest.writeInt(trailBits);
	}

	public byte[] getBits() {
		return bits;
	}

	public void setBits(byte[] bits) {
		this.bits = bits;
	}

	public int getTrailBits() {
		return trailBits;
	}

	public void setTrailBits(int trailBits) {
		this.trailBits = trailBits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bits);
		result = prime * result + trailBits;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IBITSTRING))
			return false;
		IBITSTRING other = (IBITSTRING) obj;
		if (!Arrays.equals(bits, other.bits))
			return false;
		if (trailBits != other.trailBits)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IBITSTRING [bits=" + Arrays.toString(bits) + ", trailBits="
				+ trailBits + "]";
	}

}
