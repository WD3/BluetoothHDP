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

public class IConfigId implements Parcelable {

	public static final int manager_config_response	=	0;
	public static final int standard_config_start	=	1;
	public static final int standard_config_end		=	16383;
	public static final int extended_config_start	=	16384;
	public static final int extended_config_end		=	32767;
	public static final int reserved_start			=	32768;
	public static final int reserved_end			=	65535;

	private int configId;

	public static final Parcelable.Creator<IConfigId> CREATOR =
			new Parcelable.Creator<IConfigId>() {
		public IConfigId createFromParcel(Parcel in) {
			return new IConfigId(in);
		}

		public IConfigId[] newArray(int size) {
			return new IConfigId[size];
		}
	};

	public IConfigId () {

	}

	private IConfigId (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(configId);
	}

	public void readFromParcel(Parcel in) {
		configId = in.readInt();
	}

	public IConfigId (int configId) {
		this.configId = configId;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + configId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IConfigId))
			return false;
		IConfigId other = (IConfigId) obj;
		if (configId != other.configId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String msg = "0x" + Integer.toHexString(configId);

		switch (configId) {
			case manager_config_response: msg += ": manager_config_response"; break;
			case standard_config_start:	msg += ": standard_config_start"; break;
			case standard_config_end: msg += ": standard_config_end"; break;
			case extended_config_start:	msg += ": extended_config_start"; break;
			case extended_config_end: msg += ": extended_config_end"; break;
			case reserved_start: msg += ": reserved_start"; break;
			case reserved_end: msg += ": reserved_end"; break;
		}
		return msg;
	}

}