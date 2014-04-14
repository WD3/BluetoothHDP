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

import android.os.Parcel;
import android.os.Parcelable;

public class IINT_U32 implements Parcelable {
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public static final Parcelable.Creator<IINT_U32> CREATOR = new Parcelable.Creator<IINT_U32>() {
		public IINT_U32 createFromParcel(Parcel in) {
			return new IINT_U32(in);
		}

		public IINT_U32[] newArray(int size) {
			return new IINT_U32[size];
		}
	};

	public IINT_U32() {

	}

	public IINT_U32(long value) {
		this.value = value;
	}

	private IINT_U32(Parcel in) {
		value = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(value);
	}

	public boolean equals(Object o) {
		if (!(o instanceof IINT_U32))
			return false;

		IINT_U32 agent = (IINT_U32) o;
		return this.value == agent.value;
	}

	@Override
	public String toString() {
		return new Long(value).toString();
	}

}
