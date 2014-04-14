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

import android.os.Parcel;
import android.os.Parcelable;

public class IValueMeasure extends IMeasure implements Parcelable {
	//internal value representation by exponent and mantissa (float is not needed to be pass from ipc call)
	private int value_exp;
	private int value_mag;
	private double value;
	private String valueStr;

	public static final Parcelable.Creator<IValueMeasure> CREATOR =
			new Parcelable.Creator<IValueMeasure>() {
		public IValueMeasure createFromParcel(Parcel in) {
			return new IValueMeasure(in);
		}

		public IValueMeasure[] newArray(int size) {
			return new IValueMeasure[size];
		}
	};

	private IValueMeasure (Parcel in){
		super(in);
		value_exp = in.readInt();
		value_mag = in.readInt();
		value = in.readDouble();
		valueStr = in.readString();
	}

	public IValueMeasure (int mType, int exp, int mag, double value, String valueStr){
		super(mType);
		this.value_exp = exp;
		this.value_mag = mag;
		this.value = value;
		this.valueStr = valueStr;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(value_exp);
		dest.writeInt(value_mag);
		dest.writeDouble(value);
		dest.writeString(valueStr);
	}

	public double getFloatType () {
		return value;
	}

	public String toString(){
		return valueStr;
	}
}
