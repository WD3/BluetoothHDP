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

public class ISegmEntryElem implements Parcelable {
	private IOID_Type classId = null;
	private ITYPE metricType = null;
	private IHANDLE handle = null;
	private IAttrValMap attrValMap = null;

	public static final Parcelable.Creator<ISegmEntryElem> CREATOR =
			new Parcelable.Creator<ISegmEntryElem>() {
		public ISegmEntryElem createFromParcel(Parcel in) {
			return new ISegmEntryElem(in);
		}

		public ISegmEntryElem[] newArray(int size) {
			return new ISegmEntryElem[size];
		}
	};

	public ISegmEntryElem () {

	}

	private ISegmEntryElem (Parcel in) {
		readFromParcel(in);
	}

	public ISegmEntryElem (IOID_Type classId, ITYPE metricType, IHANDLE handle, IAttrValMap attrValMap) {
		this.classId = classId;
		this.metricType = metricType;
		this.handle = handle;
		this.attrValMap = attrValMap;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(classId, 0);
		dest.writeParcelable(metricType, 0);
		dest.writeParcelable(handle, 0);
		dest.writeParcelable(attrValMap, 0);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		classId = in.readParcelable(cl);
		metricType = in.readParcelable(cl);
		handle = in.readParcelable(cl);
		attrValMap = in.readParcelable(cl);
	}

	public IOID_Type getClassId() {
		return classId;
	}

	public void setClassId(IOID_Type classId) {
		this.classId = classId;
	}

	public ITYPE getMetricType() {
		return metricType;
	}

	public void setMetricType(ITYPE metricType) {
		this.metricType = metricType;
	}

	public IHANDLE getHandle() {
		return handle;
	}

	public void setHandle(IHANDLE handle) {
		this.handle = handle;
	}

	public IAttrValMap getAttrValMap() {
		return attrValMap;
	}

	public void setAttrValMap(IAttrValMap attrValMap) {
		this.attrValMap = attrValMap;
	}

	@Override
	public String toString() {
		String str = "classId = " + this.classId.toString() + " " +
						"metricType = " + this.metricType.toString() + " " +
						"handle = " + this.handle.toString() + " " +
						"attrValMap = " + this.attrValMap.toString();
		return str;
	}

}
