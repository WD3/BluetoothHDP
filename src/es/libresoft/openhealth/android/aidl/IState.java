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

public class IState implements Parcelable {

	/* copy from State class of package ieee_11073.part_20601.fsm it must be synchronized */
	public static final int DISCONNECTED										= 1;
	public static final int CONNECTED_UNASSOCIATED								= 2;
	public static final int CONNECTED_ASSOCIATING								= 3;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_SENDING_CONFIG		= 4;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_WAITING_APPROVAL	= 5;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_WAITING			= 6;
	public static final int CONNECTED_ASSOCIATED_CONFIGURING_CHECKING_CONFIG	= 7;
	public static final int CONNECTED_ASSOCIATED_OPERATING						= 8;
	public static final int CONNECTED_DISASSOCIATING							= 9;

	private int stateCode = 1;//DISCONNECTED;
	private String stateName = "";

	public static final Parcelable.Creator<IState> CREATOR =
			new Parcelable.Creator<IState>() {
		public IState createFromParcel(Parcel in) {
			return new IState(in);
		}

		public IState[] newArray(int size) {
			return new IState[size];
		}
	};

	public IState() {
		this.stateCode = 1;
		this.stateName = "";
	}

	private IState (Parcel in) {
		readFromParcel(in);
	}

	public IState(int stCode, String stName) {
		this.stateCode = stCode;
		this.stateName = stName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		stateCode = in.readInt();
		stateName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(stateCode);
		dest.writeString(stateName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stateCode;
		result = prime * result
				+ ((stateName == null) ? 0 : stateName.hashCode());
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
		IState other = (IState) obj;
		if (stateCode != other.stateCode)
			return false;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		return true;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
