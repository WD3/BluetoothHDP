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

public class IAttrValMapEntry implements Parcelable {
	private IOID_Type attributeId;
	private int attributeLen; 

	public static final Parcelable.Creator<IAttrValMapEntry> CREATOR =
			new Parcelable.Creator<IAttrValMapEntry>() {
		public IAttrValMapEntry createFromParcel(Parcel in) {
			return new IAttrValMapEntry(in);
		}

		public IAttrValMapEntry[] newArray(int size) {
			return new IAttrValMapEntry[size];
		}
	};

	public IAttrValMapEntry () {

	}

	private IAttrValMapEntry (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(attributeId, 0);
		dest.writeInt(attributeLen);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		attributeId = in.readParcelable(cl); 
		attributeLen = in.readInt();
	}

	public IAttrValMapEntry (IOID_Type attId, int attLen) {
		this.attributeId = attId;
		this.attributeLen = attLen;
	}

	public IOID_Type getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(IOID_Type attributeId) {
		this.attributeId = attributeId;
	}

	public int getAttributeLen() {
		return attributeLen;
	}

	public void setAttributeLen(int attributeLen) {
		this.attributeLen = attributeLen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeId == null) ? 0 : attributeId.hashCode());
		result = prime * result + attributeLen;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IAttrValMapEntry))
			return false;
		IAttrValMapEntry other = (IAttrValMapEntry) obj;
		if (attributeId == null) {
			if (other.attributeId != null)
				return false;
		} else if (!attributeId.equals(other.attributeId))
			return false;
		if (attributeLen != other.attributeLen)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IAttrValMapEntry [attributeId=" + attributeId
				+ ", attributeLen=" + attributeLen + "]";
	}

}
