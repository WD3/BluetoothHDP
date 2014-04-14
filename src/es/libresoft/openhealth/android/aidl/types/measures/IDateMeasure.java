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

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class IDateMeasure extends IMeasure implements Parcelable{

	//internal value representation of the date
	private long timestamp;
	private Date date;

	public static final Parcelable.Creator<IDateMeasure> CREATOR =
			new Parcelable.Creator<IDateMeasure>() {
		public IDateMeasure createFromParcel(Parcel in) {
			return new IDateMeasure(in);
		}

		public IDateMeasure[] newArray(int size) {
			return new IDateMeasure[size];
		}
	};

	private IDateMeasure (Parcel in){
		super(in);
		timestamp = in.readLong();
		date = new Date(timestamp);
	}

	public IDateMeasure (int mType, long timestamp){
		super(mType);
		this.timestamp = timestamp;
		date = new Date(timestamp);
	}

	public Date getTimeStamp(){
		return date;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(timestamp);
	}


	public String toString(){
		SimpleDateFormat sdf =  new SimpleDateFormat("yy/MM/dd HH:mm:ss:SS");
		return sdf.format(timestamp);
	}
}
