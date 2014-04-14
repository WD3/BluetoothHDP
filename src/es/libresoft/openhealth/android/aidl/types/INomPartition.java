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

public class INomPartition implements Parcelable {

	public final int nom_part_unspec		=	0;
	/** object-oriented partition */
	public final int nom_part_obj			=	1;
	/** metric [supervisory control and data acquisition (SCADA)] partition */
	public final int nom_part_metric		=	2;
	/** alerts/events partition */
	public final int nom_part_alert		=	3;
	/** dimensions partition */
	public final int nom_part_dim			=	4;
	/** virtual attribute partition for Operation objects */
	public final int nom_part_vattr		=	5;
	/** parameter group ID partition */
	public final int nom_part_pgrp			=	6;
	/** measurement and body site locations */
	public final int nom_part_sites		=	7;
	/** infrastructure elements partition */
	public final int nom_part_infrastruct	=	8;
	/** file exchange format partition */
	public final int nom_part_fef			=	9;
	/** ECG extensions partition */
	public final int nom_part_ecg_extn		=	10;
	/** Disease Mgmt (New, needs to be added) */
	public final int nom_part_phd_dm		=	128;
	/** Health & Fitness (New, needs to be added) */
	public final int nom_part_phd_hf		=	129;
	/** Aging Independently (New, needs to be added) */
	public final int nom_part_phd_ai		=	130;
	/** return codes partition (New, needs to be added) */
	public final int nom_part_ret_code		=	255;
	/** IDs of other nomenclatures and dictionaries */
	public final int nom_part_ext_nom		=	256;
	/** private partition */
	public final int nom_part_priv			=	1024;


	private int nomPart;

	public static final Parcelable.Creator<INomPartition> CREATOR =
			new Parcelable.Creator<INomPartition>() {
		public INomPartition createFromParcel(Parcel in) {
			return new INomPartition(in);
		}

		public INomPartition[] newArray(int size) {
			return new INomPartition[size];
		}
	};

	public INomPartition () {

	}

	public INomPartition (int nomPart) {
		this.nomPart = nomPart;
	}

	private INomPartition (Parcel in) {
		nomPart = in.readInt();
	}

	public int getNomPart() {
		return nomPart;
	}

	public void setNomPart(int nomPart) {
		this.nomPart = nomPart;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(nomPart);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nomPart;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof INomPartition))
			return false;

		INomPartition other = (INomPartition) obj;
		if (nomPart != other.nomPart)
			return false;

		return true;
	}

}
