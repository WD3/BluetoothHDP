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

public class IBatMeasure implements Parcelable {
	private IFLOAT_Type value;
	private IOID_Type unit;

	public static final Parcelable.Creator<IBatMeasure> CREATOR =
			new Parcelable.Creator<IBatMeasure>() {
		public IBatMeasure createFromParcel(Parcel in) {
			return new IBatMeasure(in);
		}

		public IBatMeasure[] newArray(int size) {
			return new IBatMeasure[size];
		}
	};

	public IBatMeasure () {

	}

	public IBatMeasure(IFLOAT_Type value, IOID_Type unit) {
		this.value = value;
		this.unit = unit;
	}

	private IBatMeasure (Parcel in) {
		ClassLoader cl = this.getClass().getClassLoader();
		value = in.readParcelable(cl);
		unit = in.readParcelable(cl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(value, 0);
		dest.writeParcelable(unit, 0);
	}

	public IFLOAT_Type getValue() {
		return value;
	}

	public void setValue(IFLOAT_Type value) {
		this.value = value;
	}

	public IOID_Type getUnit() {
		return unit;
	}

	public void setUnit(IOID_Type unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IBatMeasure))
			return false;
		IBatMeasure other = (IBatMeasure) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
