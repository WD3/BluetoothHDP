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

import es.libresoft.openhealth.android.aidl.IAgent;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IError;
import es.libresoft.openhealth.android.aidl.types.objects.IDIMClass;
import es.libresoft.openhealth.android.aidl.types.objects.IMDS;
import es.libresoft.openhealth.android.aidl.types.objects.INumeric;
import es.libresoft.openhealth.android.aidl.types.objects.IScanner;
import es.libresoft.openhealth.android.aidl.types.objects.IRT_SA;
import es.libresoft.openhealth.android.aidl.types.objects.IEnumeration;
import es.libresoft.openhealth.android.aidl.types.objects.IPM_Store;
import es.libresoft.openhealth.android.aidl.IState;

interface IAgentService {
	boolean updateMDS(in IAgent agent, out IError err);
	void getState(in IAgent agent, out IState state, out IError err);

	void getMDS(in IAgent agent, out IMDS mds, out IError error);
	void getNumeric(in IAgent agent, out List<INumeric> nums, out IError error);
	void getScanner(in IAgent agent, out List<IScanner> scanners, out IError error);
	void getRT_SA(in IAgent agent, out List<IRT_SA> rts, out IError error);
	void getEnumeration(in IAgent agent, out List<IEnumeration> nums, out IError error);
	void getPM_Store(in IAgent agent, out List<IPM_Store> nums, out IError error);

	void getObjectAttrs(in IDIMClass obj, out List<IAttribute> attrs, out IError error);

	boolean setTime(in IAgent agent, out IError err);

	void connect(in IAgent agent);
	boolean disconnect(in IAgent agent, out IError err);
}