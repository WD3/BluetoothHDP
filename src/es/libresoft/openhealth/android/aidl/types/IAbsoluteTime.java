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

public class IAbsoluteTime implements Parcelable {
	private int century;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int secFractions;

	public static final Parcelable.Creator<IAbsoluteTime> CREATOR =
			new Parcelable.Creator<IAbsoluteTime>() {
		public IAbsoluteTime createFromParcel(Parcel in) {
			return new IAbsoluteTime(in);
		}

		public IAbsoluteTime[] newArray(int size) {
			return new IAbsoluteTime[size];
		}
	};

	public IAbsoluteTime () {

	}

	public IAbsoluteTime(int century, int year, int month, int day, int hour,
						int minute, int second, int secFractions) {
		this.century = century;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.secFractions = secFractions;
	}

	private IAbsoluteTime (Parcel in) {
		century = in.readInt();
		year = in.readInt();
		month = in.readInt();
		day = in.readInt();
		hour = in.readInt();
		minute = in.readInt();
		second = in.readInt();
		secFractions = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(century);
		dest.writeInt(year);
		dest.writeInt(month);
		dest.writeInt(day);
		dest.writeInt(hour);
		dest.writeInt(minute);
		dest.writeInt(second);
		dest.writeInt(secFractions);
	}

	public int getCentury() {
		return century;
	}

	public void setCentury(int century) {
		this.century = century;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getSecFractions() {
		return secFractions;
	}

	public void setSecFractions(int secFractions) {
		this.secFractions = secFractions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + century;
		result = prime * result + day;
		result = prime * result + hour;
		result = prime * result + minute;
		result = prime * result + month;
		result = prime * result + secFractions;
		result = prime * result + second;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IAbsoluteTime))
			return false;
		IAbsoluteTime other = (IAbsoluteTime) obj;
		if (century != other.century)
			return false;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (minute != other.minute)
			return false;
		if (month != other.month)
			return false;
		if (secFractions != other.secFractions)
			return false;
		if (second != other.second)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str;

		/* MM/DD/YY_HH:MM:SS_FF */
		str = month + "/" + day + "/" + year;
		str += "_" + hour + ":" + minute + ":" + second + "_" + secFractions;
		return str;
	}
}
