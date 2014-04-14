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

public class IMdsTimeInfo implements Parcelable {
	private IMdsTimeCapState mdsTimeCapState;
	private IOID_Type timeSyncProtocol;
	private IRelativeTime timeSyncAccuracy;
	private int timeResolutionAbsTime;
	private int timeResolutionRelTime;
	private long timeResolutionHighResTime;

	public static final Parcelable.Creator<IMdsTimeInfo> CREATOR =
			new Parcelable.Creator<IMdsTimeInfo>() {
		public IMdsTimeInfo createFromParcel(Parcel in) {
			return new IMdsTimeInfo(in);
		}

		public IMdsTimeInfo[] newArray(int size) {
			return new IMdsTimeInfo[size];
		}
	};

	public IMdsTimeInfo () {

	}

	private IMdsTimeInfo (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mdsTimeCapState, 0);
		dest.writeParcelable(timeSyncProtocol, 0);
		dest.writeParcelable(timeSyncAccuracy, 0);
		dest.writeInt(timeResolutionAbsTime);
		dest.writeInt(timeResolutionRelTime);
		dest.writeLong(timeResolutionHighResTime);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		mdsTimeCapState = in.readParcelable(cl);
		timeSyncProtocol = in.readParcelable(cl);
		timeSyncAccuracy = in.readParcelable(cl);
		timeResolutionAbsTime = in.readInt();
		timeResolutionRelTime = in.readInt();
		timeResolutionHighResTime = in.readLong();
	}

	public IMdsTimeInfo(IMdsTimeCapState mdsTimeCapState,
					IOID_Type timeSyncProtocol, IRelativeTime timeSyncAccuracy,
					int timeResolutionAbsTime, int timeResolutionRelTime,
					long timeResolutionHighResTime) {
		this.mdsTimeCapState = mdsTimeCapState;
		this.timeSyncProtocol = timeSyncProtocol;
		this.timeSyncAccuracy = timeSyncAccuracy;
		this.timeResolutionAbsTime = timeResolutionAbsTime;
		this.timeResolutionRelTime = timeResolutionRelTime;
		this.timeResolutionHighResTime = timeResolutionHighResTime;
	}

	public IMdsTimeCapState getMdsTimeCapState() {
		return mdsTimeCapState;
	}

	public void setMdsTimeCapState(IMdsTimeCapState mdsTimeCapState) {
		this.mdsTimeCapState = mdsTimeCapState;
	}

	public IOID_Type getTimeSyncProtocol() {
		return timeSyncProtocol;
	}

	public void setTimeSyncProtocol(IOID_Type timeSyncProtocol) {
		this.timeSyncProtocol = timeSyncProtocol;
	}

	public IRelativeTime getTimeSyncAccuracy() {
		return timeSyncAccuracy;
	}

	public void setTimeSyncAccuracy(IRelativeTime timeSyncAccuracy) {
		this.timeSyncAccuracy = timeSyncAccuracy;
	}

	public int getTimeResolutionAbsTime() {
		return timeResolutionAbsTime;
	}

	public void setTimeResolutionAbsTime(int timeResolutionAbsTime) {
		this.timeResolutionAbsTime = timeResolutionAbsTime;
	}

	public int getTimeResolutionRelTime() {
		return timeResolutionRelTime;
	}

	public void setTimeResolutionRelTime(int timeResolutionRelTime) {
		this.timeResolutionRelTime = timeResolutionRelTime;
	}

	public long getTimeResolutionHighResTime() {
		return timeResolutionHighResTime;
	}

	public void setTimeResolutionHighResTime(long timeResolutionHighResTime) {
		this.timeResolutionHighResTime = timeResolutionHighResTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mdsTimeCapState == null) ? 0 : mdsTimeCapState.hashCode());
		result = prime * result + timeResolutionAbsTime;
		result = prime
				* result
				+ (int) (timeResolutionHighResTime ^ (timeResolutionHighResTime >>> 32));
		result = prime * result + timeResolutionRelTime;
		result = prime
				* result
				+ ((timeSyncAccuracy == null) ? 0 : timeSyncAccuracy.hashCode());
		result = prime
				* result
				+ ((timeSyncProtocol == null) ? 0 : timeSyncProtocol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IMdsTimeInfo))
			return false;
		IMdsTimeInfo other = (IMdsTimeInfo) obj;
		if (mdsTimeCapState == null) {
			if (other.mdsTimeCapState != null)
				return false;
		} else if (!mdsTimeCapState.equals(other.mdsTimeCapState))
			return false;
		if (timeResolutionAbsTime != other.timeResolutionAbsTime)
			return false;
		if (timeResolutionHighResTime != other.timeResolutionHighResTime)
			return false;
		if (timeResolutionRelTime != other.timeResolutionRelTime)
			return false;
		if (timeSyncAccuracy == null) {
			if (other.timeSyncAccuracy != null)
				return false;
		} else if (!timeSyncAccuracy.equals(other.timeSyncAccuracy))
			return false;
		if (timeSyncProtocol == null) {
			if (other.timeSyncProtocol != null)
				return false;
		} else if (!timeSyncProtocol.equals(other.timeSyncProtocol))
			return false;
		return true;
	}

}
