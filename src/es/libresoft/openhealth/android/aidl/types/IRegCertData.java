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

public class IRegCertData implements Parcelable {
	private IAuthBodyAndStrucType authBodyAndStrucType;
	private Parcelable authBodyData;

	public static final Parcelable.Creator<IRegCertData> CREATOR =
			new Parcelable.Creator<IRegCertData>() {
		public IRegCertData createFromParcel(Parcel in) {
			return new IRegCertData(in);
		}

		public IRegCertData[] newArray(int size) {
			return new IRegCertData[size];
		}
	};

	public IRegCertData () {

	}

	public IRegCertData(IAuthBodyAndStrucType authBodyAndStrucType,
							Parcelable authBodyData) {
		this.authBodyAndStrucType = authBodyAndStrucType;
		this.authBodyData = authBodyData;
	}

	private IRegCertData (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(authBodyAndStrucType, 0);
		dest.writeParcelable(authBodyData, 0);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		authBodyAndStrucType = in.readParcelable(cl);
		authBodyData = in.readParcelable(cl);
	}

	public IAuthBodyAndStrucType getAuthBodyAndStrucType() {
		return authBodyAndStrucType;
	}

	public void setAuthBodyAndStrucType(IAuthBodyAndStrucType authBodyAndStrucType) {
		this.authBodyAndStrucType = authBodyAndStrucType;
	}

	public Parcelable getAuthBodyData() {
		return authBodyData;
	}

	public void setAuthBodyData(Parcelable authBodyData) {
		this.authBodyData = authBodyData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((authBodyAndStrucType == null) ? 0 : authBodyAndStrucType
						.hashCode());
		result = prime * result
				+ ((authBodyData == null) ? 0 : authBodyData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IRegCertData))
			return false;
		IRegCertData other = (IRegCertData) obj;
		if (authBodyAndStrucType == null) {
			if (other.authBodyAndStrucType != null)
				return false;
		} else if (!authBodyAndStrucType.equals(other.authBodyAndStrucType))
			return false;
		if (authBodyData == null) {
			if (other.authBodyData != null)
				return false;
		} else if (!authBodyData.equals(other.authBodyData))
			return false;
		return true;
	}

}
