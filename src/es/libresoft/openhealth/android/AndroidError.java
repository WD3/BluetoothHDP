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

import com.example.bluetooth.health.R;

import android.content.Context;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.error.ErrorString;

public class AndroidError implements ErrorString {

	Context context;

	public AndroidError(Context context) {
		this.context = context;
	}

	@Override
	public String error2string(int errCode) {
		switch (errCode) {
		case ErrorCodes.NO_ERROR:
			return context.getString(R.string.NO_ERROR);
		case ErrorCodes.TIMEOUT:
			return context.getString(R.string.TIMEOUT);
		case ErrorCodes.TIMEOUT_ASSOC:
			return context.getString(R.string.TIMEOUT_ASSOC);
		case ErrorCodes.TIMEOUT_CONFIG:
			return context.getString(R.string.TIMEOUT_CONFIG);
		case ErrorCodes.TIMEOUT_ASSOC_REL:
			return context.getString(R.string.TIMEOUT_ASSOC_REL);
		case ErrorCodes.TIMEOUT_MDS_CONF_ACION:
			return context.getString(R.string.TIMEOUT_MDS_CONF_ACION);
		case ErrorCodes.TIMEOUT_MDS_CONF_EV_REP:
			return context.getString(R.string.TIMEOUT_MDS_CONF_EV_REP);
		case ErrorCodes.TIMEOUT_MDS_GET:
			return context.getString(R.string.TIMEOUT_MDS_GET);
		case ErrorCodes.TIMEOUT_MDS_CONF_SET:
			return context.getString(R.string.TIMEOUT_MDS_CONF_SET);
		case ErrorCodes.TIMEOUT_MDS_SPECIAL:
			return context.getString(R.string.TIMEOUT_MDS_SPECIAL);
		case ErrorCodes.TIMEOUT_PM_CONF_ACION:
			return context.getString(R.string.TIMEOUT_PM_CONF_ACION);
		case ErrorCodes.TIMEOUT_PM_CONF_EV_REP:
			return context.getString(R.string.TIMEOUT_PM_CONF_EV_REP);
		case ErrorCodes.TIMEOUT_PM_GET:
			return context.getString(R.string.TIMEOUT_PM_GET);
		case ErrorCodes.TIMEOUT_PM_CONF_SET:
			return context.getString(R.string.TIMEOUT_PM_CONF_SET);
		case ErrorCodes.TIMEOUT_PM_SPECIAL:
			return context.getString(R.string.TIMEOUT_PM_SPECIAL);
		case ErrorCodes.TIMEOUT_SCN_CONF_SET:
			return context.getString(R.string.TIMEOUT_SCN_CONF_SET);
		case ErrorCodes.TIMEOUT_SCN_CONF_EV_REP:
			return context.getString(R.string.TIMEOUT_SCN_CONF_EV_REP);
		case ErrorCodes.UNKNOWN_AGENT:
			return context.getString(R.string.UNKNOWN_AGENT);
		case ErrorCodes.INVALID_ATTRIBUTE:
			return context.getString(R.string.INVALID_ATTRIBUTE);
		case ErrorCodes.UNKNOWN_OBJECT:
			return context.getString(R.string.UNKNOWN_OBJECT);
		case ErrorCodes.INVALID_ARGUMENTS:
			return context.getString(R.string.INVALID_ARGUMENTS);
		case ErrorCodes.UNKNOWN_PMSTORE:
			return context.getString(R.string.UNKNOWN_PMSTORE);
		case ErrorCodes.ASSOCIATION_ERROR:
			return context.getString(R.string.ASSOCIATION_ERROR);
		default:
			return context.getString(R.string.UNEXPECTED_ERROR);
		}
	}
}
