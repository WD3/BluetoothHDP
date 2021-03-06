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

public class IBasicNuObsValue implements Parcelable {
	private ISFloatType value;

	public static final Parcelable.Creator<IBasicNuObsValue> CREATOR =
			new Parcelable.Creator<IBasicNuObsValue>() {
		public IBasicNuObsValue createFromParcel(Parcel in) {
			return new IBasicNuObsValue(in);
		}

		public IBasicNuObsValue[] newArray(int size) {
			return new IBasicNuObsValue[size];
		}
	};

	private IBasicNuObsValue (Parcel in){
		ClassLoader cl = getClass().getClassLoader();
		value = in.readParcelable(cl);
	}

	public IBasicNuObsValue (ISFloatType value){
		this.value = value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(value, 0);
	}

	public ISFloatType getValue () {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IBasicNuObsValue))
			return false;
		IBasicNuObsValue other = (IBasicNuObsValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IBasicNuObsValue [value=" + value + "]";
	}

}
