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

public class IPmSegmentEntryMap implements Parcelable {
	private ISegmEntryHeader segmEntryHeader;
	private ISegmEntryElemList segmEntryElemList;

	public static final Parcelable.Creator<IPmSegmentEntryMap> CREATOR =
			new Parcelable.Creator<IPmSegmentEntryMap>() {
		public IPmSegmentEntryMap createFromParcel(Parcel in) {
			return new IPmSegmentEntryMap(in);
		}

		public IPmSegmentEntryMap[] newArray(int size) {
			return new IPmSegmentEntryMap[size];
		}
	};

	public IPmSegmentEntryMap () {

	}

	public IPmSegmentEntryMap (ISegmEntryHeader segmEntryHeader, ISegmEntryElemList segmEntryElemList) {
		this.segmEntryHeader = segmEntryHeader;
		this.segmEntryElemList = segmEntryElemList;
	}

	private IPmSegmentEntryMap (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(segmEntryHeader, 0);
		dest.writeParcelable(segmEntryElemList, 0);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		segmEntryHeader = in.readParcelable(cl);
		segmEntryElemList = in.readParcelable(cl);
	}

	public ISegmEntryHeader getSegmEntryHeader() {
		return segmEntryHeader;
	}

	public void setSegmEntryHeader(ISegmEntryHeader segmEntryHeader) {
		this.segmEntryHeader = segmEntryHeader;
	}

	public ISegmEntryElemList getSegmEntryElemList() {
		return segmEntryElemList;
	}

	public void setSegmEntryElemList(ISegmEntryElemList segmEntryElemList) {
		this.segmEntryElemList = segmEntryElemList;
	}

	@Override
	public String toString() {
		return "IPmSegmentEntryMap [segmEntryHeader=" + segmEntryHeader.toString()
				+ ", segmEntryElemList=" + segmEntryElemList.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((segmEntryElemList == null) ? 0 : segmEntryElemList
						.hashCode());
		result = prime * result
				+ ((segmEntryHeader == null) ? 0 : segmEntryHeader.hashCode());
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
		IPmSegmentEntryMap other = (IPmSegmentEntryMap) obj;
		if (segmEntryElemList == null) {
			if (other.segmEntryElemList != null)
				return false;
		} else if (!segmEntryElemList.equals(other.segmEntryElemList))
			return false;
		if (segmEntryHeader == null) {
			if (other.segmEntryHeader != null)
				return false;
		} else if (!segmEntryHeader.equals(other.segmEntryHeader))
			return false;
		return true;
	}

}
