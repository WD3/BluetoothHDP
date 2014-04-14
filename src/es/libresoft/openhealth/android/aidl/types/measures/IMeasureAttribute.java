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

public class IMeasureAttribute implements Parcelable {

	private int type; /* Id type */
	private int code; /* value */
	private String typeString;
	private String codeString;

	public static final Parcelable.Creator<IMeasureAttribute> CREATOR =
			new Parcelable.Creator<IMeasureAttribute>() {
		public IMeasureAttribute createFromParcel(Parcel in) {
			return new IMeasureAttribute(in);
		}

		public IMeasureAttribute[] newArray(int size) {
			return new IMeasureAttribute[size];
		}
	};

	private IMeasureAttribute (Parcel in){
		type = in.readInt();
		code = in.readInt();
		typeString = in.readString();
		codeString = in.readString();
	}

	public IMeasureAttribute (int type, int code, String typeString, String codeString){
		this.type = type;
		this.code = code;
		this.typeString = typeString;
		this.codeString = codeString;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeInt(code);
		dest.writeString(typeString);
		dest.writeString(codeString);
	}

	public int getAttrId () {
		return this.type;
	}

	public int getCode () {
		return this.code;
	}

	public String getAttrIdStr() {
		return typeString;
	}

	public String getCodeStr() {
		return this.codeString;
	}

}
