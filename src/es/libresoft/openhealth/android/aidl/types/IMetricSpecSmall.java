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

public class IMetricSpecSmall implements Parcelable {
	private IBITSTRING bits;

	public static final Parcelable.Creator<IMetricSpecSmall> CREATOR =
			new Parcelable.Creator<IMetricSpecSmall>() {
		public IMetricSpecSmall createFromParcel(Parcel in) {
			return new IMetricSpecSmall(in);
		}

		public IMetricSpecSmall[] newArray(int size) {
			return new IMetricSpecSmall[size];
		}
	};

	public IMetricSpecSmall () {

	}

	public IMetricSpecSmall (IBITSTRING bits) {
		this.bits = bits;
	}

	private IMetricSpecSmall (Parcel in) {
		ClassLoader cl = this.getClass().getClassLoader();
		this.bits = in.readParcelable(cl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(bits, 0);
	}

	public IBITSTRING getBits() {
		return bits;
	}

	public void setBits(IBITSTRING bits) {
		this.bits = bits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bits == null) ? 0 : bits.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IMetricSpecSmall))
			return false;
		IMetricSpecSmall other = (IMetricSpecSmall) obj;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IMetricSpecSmall [bits=" + bits + "]";
	}

}
