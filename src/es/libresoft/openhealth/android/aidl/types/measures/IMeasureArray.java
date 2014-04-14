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

package es.libresoft.openhealth.android.aidl.types.measures;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class IMeasureArray extends IMeasure implements Parcelable {
	private List<IMeasure> list = new ArrayList<IMeasure>();

	public static final Parcelable.Creator<IMeasureArray> CREATOR =
								new Parcelable.Creator<IMeasureArray>() {
		public IMeasureArray createFromParcel(Parcel in) {
			return new IMeasureArray(in);
		}

		public IMeasureArray[] newArray(int size) {
			return new IMeasureArray[size];
		}
	};

	private IMeasureArray (Parcel in){
		super(in);
		ClassLoader cl = this.getClass().getClassLoader();
		in.readList(list, cl);
	}

	public IMeasureArray (int mType, List<IMeasure> values) {
		super(mType);
		list = values;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeList(list);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IMeasureArray))
			return false;
		IMeasureArray other = (IMeasureArray) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}

	public List<IMeasure> getList() {
		return list;
	}

	@Override
	public String toString() {
		return "IMeasureArray [list=" + list + "]";
	}

}
