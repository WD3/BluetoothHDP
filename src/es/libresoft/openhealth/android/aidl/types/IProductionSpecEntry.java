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

public class IProductionSpecEntry implements Parcelable {
	private int specType;
	private IPrivateOid componentId;
	private IOCTETSTRING prodSpec;

	public static final Parcelable.Creator<IProductionSpecEntry> CREATOR =
			new Parcelable.Creator<IProductionSpecEntry>() {
		public IProductionSpecEntry createFromParcel(Parcel in) {
			return new IProductionSpecEntry(in);
		}

		public IProductionSpecEntry[] newArray(int size) {
			return new IProductionSpecEntry[size];
		}
	};

	public IProductionSpecEntry () {

	}

	private IProductionSpecEntry (Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(specType);
		dest.writeParcelable(componentId, 0);
		dest.writeParcelable(prodSpec, 0);
	}

	public void readFromParcel(Parcel in) {
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		specType = in.readInt();
		componentId = in.readParcelable(cl);
		prodSpec = in.readParcelable(cl);
	}

	public IProductionSpecEntry (int specType, IPrivateOid componentId, IOCTETSTRING prodSpec) {
		this.specType = specType;
		this.componentId = componentId;
		this.prodSpec = prodSpec;
	}

	public int getSpecType() {
		return specType;
	}

	public void setSpecType(int specType) {
		this.specType = specType;
	}

	public IPrivateOid getComponentId() {
		return componentId;
	}

	public void setComponentId(IPrivateOid componentId) {
		this.componentId = componentId;
	}

	public IOCTETSTRING getProdSpec() {
		return prodSpec;
	}

	public void setProdSpec(IOCTETSTRING prodSpec) {
		this.prodSpec = prodSpec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result
				+ ((prodSpec == null) ? 0 : prodSpec.hashCode());
		result = prime * result + specType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IProductionSpecEntry))
			return false;
		IProductionSpecEntry other = (IProductionSpecEntry) obj;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (prodSpec == null) {
			if (other.prodSpec != null)
				return false;
		} else if (!prodSpec.equals(other.prodSpec))
			return false;
		if (specType != other.specType)
			return false;
		return true;
	}

}
