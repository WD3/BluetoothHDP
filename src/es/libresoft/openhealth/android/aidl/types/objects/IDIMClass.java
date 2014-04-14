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

package es.libresoft.openhealth.android.aidl.types.objects;

import es.libresoft.openhealth.android.aidl.IAgent;
import android.os.Parcel;
import android.os.Parcelable;

public class IDIMClass implements Parcelable {
	private int handle;
	private IAgent agent;

	public static final Parcelable.Creator<IDIMClass> CREATOR =
			new Parcelable.Creator<IDIMClass>() {
		public IDIMClass createFromParcel(Parcel in) {
			return new IDIMClass(in);
		}

		public IDIMClass[] newArray(int size) {
			return new IDIMClass[size];
		}
	};

	protected IDIMClass () {
	}

	protected IDIMClass (Parcel in) {
		ClassLoader cl = this.getClass().getClassLoader();
		handle = in.readInt();
		agent = in.readParcelable(cl);
	}

	public IDIMClass(int handle, IAgent agent) {
		this.handle = handle;
		this.agent = agent;
	}

	public void copy(IDIMClass dim) {
		this.handle = dim.handle;
		this.agent = dim.agent;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		ClassLoader cl = IAgent.class.getClassLoader();
		handle = in.readInt();
		agent = in.readParcelable(cl);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(handle);
		dest.writeParcelable(agent, 0);
	}

	public int getHandle() {
		return handle;
	}

	public IAgent getAgent() {
		return agent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agent == null) ? 0 : agent.hashCode());
		result = prime * result + handle;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IDIMClass))
			return false;
		IDIMClass other = (IDIMClass) obj;
		if (agent == null) {
			if (other.agent != null)
				return false;
		} else if (!agent.equals(other.agent))
			return false;
		if (handle != other.handle)
			return false;
		return true;
	}

}
