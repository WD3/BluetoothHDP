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

import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.application.ExternalEvent;

import java.util.concurrent.Semaphore;

public class AndroidExternalEvent<ResponseType, PrivDataType> extends ExternalEvent<ResponseType, PrivDataType> {

	private Semaphore sem;
	private ResponseType rspData;
	private int errror;
	private boolean processed;

	public AndroidExternalEvent(int eventType, PrivDataType data) {
		super (eventType, data);
		sem = new Semaphore(0, true);
		errror = ErrorCodes.NO_ERROR;
		rspData = null;
		processed = false;
	}

	public void processed(ResponseType data, int err) {
		this.rspData = data;
		this.errror = err;
		processed = true;
		sem.release();
	}

	public void proccessing() throws InterruptedException {
		sem.acquire();
	}

	public boolean wasError() {
		if (!processed)
			return false;
		else
			return errror != ErrorCodes.NO_ERROR;
	}

	public ResponseType getRspData() {
		return rspData;
	}

	public int getError() {
		return errror;
	}
}
