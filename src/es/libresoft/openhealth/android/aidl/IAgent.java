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

package es.libresoft.openhealth.android.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IAgent implements Parcelable {
	private int id;
	private String transportDesc;

	public static final Parcelable.Creator<IAgent> CREATOR =
			new Parcelable.Creator<IAgent>() {
	    public IAgent createFromParcel(Parcel in) {
	        return new IAgent(in);
	    }

	    public IAgent[] newArray(int size) {
	        return new IAgent[size];
	    }
	};

	private IAgent (Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {
		id = in.readInt();
		transportDesc = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(transportDesc);
	}

	public IAgent () {
	}

	public IAgent (int id, String transportDesc) {
		this.id = id;
		this.transportDesc = transportDesc;
	}

	public boolean equals(Object o) {
		if (o instanceof IAgent) {
			IAgent agent = (IAgent) o;
			return this.id == agent.id;
		}

		return false;
	}

	public int getId() {
		return id;
	}

	public String getTransportDesc() {
		return transportDesc;
	}
}

