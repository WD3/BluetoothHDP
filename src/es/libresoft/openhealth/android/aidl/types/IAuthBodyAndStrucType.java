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

public class IAuthBodyAndStrucType implements Parcelable {
	private int authBody;
	private int authBodyStrucType;

	public static final Parcelable.Creator<IAuthBodyAndStrucType> CREATOR =
			new Parcelable.Creator<IAuthBodyAndStrucType>() {
		public IAuthBodyAndStrucType createFromParcel(Parcel in) {
			return new IAuthBodyAndStrucType(in);
		}

		public IAuthBodyAndStrucType[] newArray(int size) {
			return new IAuthBodyAndStrucType[size];
		}
	};

	public IAuthBodyAndStrucType () {

	}

	public IAuthBodyAndStrucType(int authBody, int authBodyStrucType) {
		this.authBody = authBody;
		this.authBodyStrucType = authBodyStrucType;
	}

	private IAuthBodyAndStrucType (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(authBody);
		dest.writeInt(authBodyStrucType);
	}

	public void readFromParcel(Parcel in) {
		authBody = in.readInt();
		authBodyStrucType = in.readInt();
	}

	public int getAuthBody() {
		return authBody;
	}

	public void setAuthBody(int authBody) {
		this.authBody = authBody;
	}

	public int getAuthBodyStrucType() {
		return authBodyStrucType;
	}

	public void setAuthBodyStrucType(int authBodyStrucType) {
		this.authBodyStrucType = authBodyStrucType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authBody;
		result = prime * result + authBodyStrucType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IAuthBodyAndStrucType))
			return false;
		IAuthBodyAndStrucType other = (IAuthBodyAndStrucType) obj;
		if (authBody != other.authBody)
			return false;
		if (authBodyStrucType != other.authBodyStrucType)
			return false;
		return true;
	}

}
