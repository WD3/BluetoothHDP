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

public class IError implements Parcelable {

	private int errCode;
	private String errMsg;

	public static final Parcelable.Creator<IError> CREATOR =
			new Parcelable.Creator<IError>() {
	    public IError createFromParcel(Parcel in) {
	        return new IError(in);
	    }
	
	    public IError[] newArray(int size) {
	        return new IError[size];
	    }
	};

	public IError () {
	}

	public IError (int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	private IError (Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {
		errCode = in.readInt();
		errMsg = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(errCode);
		dest.writeString(errMsg);
	}

	public int getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
