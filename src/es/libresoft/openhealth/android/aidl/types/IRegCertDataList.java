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

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class IRegCertDataList implements Parcelable {
	private List<IRegCertData> values = null;

	public static final Parcelable.Creator<IRegCertDataList> CREATOR =
			new Parcelable.Creator<IRegCertDataList>() {
		public IRegCertDataList createFromParcel(Parcel in) {
			return new IRegCertDataList(in);
		}

		public IRegCertDataList[] newArray(int size) {
			return new IRegCertDataList[size];
		}
	};

	public IRegCertDataList () {

	}

	private IRegCertDataList (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(values);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		if (values == null)
			values = new ArrayList<IRegCertData>();
		in.readList(values, cl);
	}

	public IRegCertDataList (List<IRegCertData> values) {
		this.values = values;
	}

}
