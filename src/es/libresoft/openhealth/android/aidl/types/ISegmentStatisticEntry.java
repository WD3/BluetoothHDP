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

public class ISegmentStatisticEntry implements Parcelable{
	private ISegmStatType segmStatType = null;
	private IOCTETSTRING segmStatEntry = null;

	public static final Parcelable.Creator<ISegmentStatisticEntry> CREATOR =
			new Parcelable.Creator<ISegmentStatisticEntry>() {
		public ISegmentStatisticEntry createFromParcel(Parcel in) {
			return new ISegmentStatisticEntry(in);
		}

		public ISegmentStatisticEntry[] newArray(int size) {
			return new ISegmentStatisticEntry[size];
		}
	};

	public ISegmentStatisticEntry () {
	}

	public ISegmentStatisticEntry (ISegmStatType segmStatType, IOCTETSTRING segmStatEntry) {
		this.segmStatType = segmStatType;
		this.segmStatEntry = segmStatEntry;
	}

	private ISegmentStatisticEntry (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(segmStatType, 0);
		dest.writeParcelable(segmStatEntry, 0);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		segmStatType = in.readParcelable(cl);
		segmStatEntry = in.readParcelable(cl);
	}

	public ISegmStatType getSegmStatType() {
		return segmStatType;
	}

	public void setSegmStatType(ISegmStatType segmStatType) {
		this.segmStatType = segmStatType;
	}

	public IOCTETSTRING getSegmStatEntry() {
		return segmStatEntry;
	}

	public void setSegmStatEntry(IOCTETSTRING segmStatEntry) {
		this.segmStatEntry = segmStatEntry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((segmStatEntry == null) ? 0 : segmStatEntry.hashCode());
		result = prime * result
				+ ((segmStatType == null) ? 0 : segmStatType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ISegmentStatisticEntry other = (ISegmentStatisticEntry) obj;
		if (segmStatEntry == null) {
			if (other.segmStatEntry != null)
				return false;
		} else if (!segmStatEntry.equals(other.segmStatEntry))
			return false;
		if (segmStatType == null) {
			if (other.segmStatType != null)
				return false;
		} else if (!segmStatType.equals(other.segmStatType))
			return false;
		return true;
	}

}
