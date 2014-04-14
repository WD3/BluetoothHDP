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

package es.libresoft.openhealth.android;

import android.os.Parcel;
import android.os.Parcelable;

public class PM_Store implements Parcelable {
	private int handler;
	private String systId;

	public static final Parcelable.Creator<PM_Store> CREATOR =
			new Parcelable.Creator<PM_Store>() {
	    public PM_Store createFromParcel(Parcel in) {
	        return new PM_Store(in);
	    }

	    public PM_Store[] newArray(int size) {
	        return new PM_Store[size];
	    }
	};

	private PM_Store (Parcel in) {
		handler = in.readInt();
		systId = in.readString();
	}

	public PM_Store(int pmHandler, String systemId) {
		handler = pmHandler;
		systId = systemId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(handler);
		dest.writeString(systId);
	}

	public int getPM_StoreHandler() {
		return handler;
	}

	public String getPM_StoreAgentId() {
		return systId;
	}
}
