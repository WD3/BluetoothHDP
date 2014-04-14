/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jorge Fernandez Gonzalez <jfernandez@libresoft.es>

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

public class IInstNumber implements Parcelable {
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static final Parcelable.Creator<IInstNumber> CREATOR =
			new Parcelable.Creator<IInstNumber>() {
		public IInstNumber[] newArray(int size) {
			return new IInstNumber[size];
		}

		@Override
		public IInstNumber createFromParcel(Parcel in) {
			return new IInstNumber(in);
		}
	};

	public IInstNumber () {

	}

	public IInstNumber (int value) {
		this.value = value;
	}

	private IInstNumber (Parcel in) {
		value = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(value);
	}

	public boolean equals(Object o) {
		if (!(o instanceof IInstNumber))
			return false;

		IInstNumber agent = (IInstNumber) o;
		return this.value == agent.value;
	}

	@Override
	public String toString() {
		return "0x" + Integer.toHexString(value);
	}
}
