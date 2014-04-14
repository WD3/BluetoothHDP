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

public class ITYPE implements Parcelable {
	private INomPartition partition;
	private IOID_Type code;
	private String repString;

	public static final Parcelable.Creator<ITYPE> CREATOR =
			new Parcelable.Creator<ITYPE>() {
		public ITYPE createFromParcel(Parcel in) {
			return new ITYPE(in);
		}

		public ITYPE[] newArray(int size) {
			return new ITYPE[size];
		}
	};

	public ITYPE () {

	}

	public ITYPE (INomPartition partition, IOID_Type code, String repString) {
		this.partition = partition;
		this.code = code;
		this.repString = repString;
	}

	private ITYPE (Parcel in) {
		ClassLoader cl = this.getClass().getClassLoader();
		partition = in.readParcelable(cl);
		code = in.readParcelable(cl);
		repString = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(partition, 0);
		dest.writeParcelable(code, 0);
		dest.writeString(repString);
	}

	public INomPartition getPartition() {
		return partition;
	}

	public void setPartition(INomPartition partition) {
		this.partition = partition;
	}

	public IOID_Type getCode() {
		return code;
	}

	public void setCode(IOID_Type code) {
		this.code = code;
	}

	public void setRepString(String repString) {
		this.repString = repString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((partition == null) ? 0 : partition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof ITYPE))
			return false;

		ITYPE other = (ITYPE) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;

		if (partition == null) {
			if (other.partition != null)
				return false;
		} else if (!partition.equals(other.partition))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return repString;
	}

}
