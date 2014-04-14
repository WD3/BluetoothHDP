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

public class IOCTETSTRING implements Parcelable {
	private byte[] octetString;

	public static final Parcelable.Creator<IOCTETSTRING> CREATOR =
			new Parcelable.Creator<IOCTETSTRING>() {
		public IOCTETSTRING createFromParcel(Parcel in) {
			return new IOCTETSTRING(in);
		}

		public IOCTETSTRING[] newArray(int size) {
			return new IOCTETSTRING[size];
		}
	};

	public IOCTETSTRING () {
	}

	private IOCTETSTRING (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByteArray(octetString);
	}

	public void readFromParcel(Parcel in) {
		octetString = in.createByteArray();
	}

	public IOCTETSTRING (byte[] octetString) {
		this.octetString = octetString;
	}

	public byte[] getOctetString() {
		return octetString;
	}

	public void setOctetString(byte[] octetString) {
		this.octetString = octetString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(octetString);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IOCTETSTRING))
			return false;
		IOCTETSTRING other = (IOCTETSTRING) obj;
		if (!Arrays.equals(octetString, other.octetString))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String msg = "0x";

		if (octetString.length == 0) return "";
		
		for (int i = 0; i < octetString.length; i++)
			msg += Integer.toHexString(octetString[i]);
		return msg;
	}

}