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

import java.util.ArrayList;
import java.util.List;

import es.libresoft.openhealth.android.aidl.types.IAttribute;

import android.os.Parcel;
import android.os.Parcelable;

public class IAgentMetric implements Parcelable {

	public ArrayList<IAttribute> attributes = new ArrayList<IAttribute>();
	public ArrayList<IMeasure> measures = new ArrayList<IMeasure>();

	public static final Parcelable.Creator<IAgentMetric> CREATOR =
			new Parcelable.Creator<IAgentMetric>() {
		public IAgentMetric createFromParcel(Parcel in) {
			return new IAgentMetric(in);
		}

		public IAgentMetric[] newArray(int size) {
			return new IAgentMetric[size];
		}
	};

	private IAgentMetric (Parcel in){
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		in.readList(attributes, cl);
		in.readList(measures, cl);
	}
	@Override
	public int describeContents() {
		return 0;
	}

	public IAgentMetric(){}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(attributes);
		dest.writeList(measures);
	}

	public void addMeasure(IMeasure obj) {
		measures.add(obj);
	}

	public void addAttribute(IAttribute obj) {
		attributes.add(obj);
	}

	public List<IAttribute> getAttributes(){
		return attributes;
	}

	public List<IMeasure> getMeasures(){
		return measures;
	}

	public void clearMeasures() {
		measures.clear();
	}
	public void clearAttributes() {
		attributes.clear();
	}
}
