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

package es.libresoft.hdp;

import java.io.IOException;

import es.libresoft.openhealth.logging.Logging;

public class HDPSession {

	/* peer field stores the underlying C++ pointer class */
	private long peer;

	private HDPCallbacks cb;

	private static native void initIDs ();
	private native void init_hdp(HDPConfig config);
	private native synchronized void HDPfree (long peer);
	private native int HDPread (long cobj, long dc, byte[] b, int offset, int length) throws IOException;
	private native void HDPwrite (long cobj, long dc, byte[] b, int offset, int count) throws IOException;

	public HDPSession(HDPConfig config, HDPCallbacks callbacks) throws Exception{
		if ((config == null) || (callbacks == null))
			throw new Exception();
		//Call to native method to start HDP session
		init_hdp(config);
		cb = callbacks;
		Runtime.getRuntime().exec("su -c /data/hdp_reg");
	}

	public void free () {
		HDPfree(peer);
	}

	protected void finalize() {
		free();
	}

	public void close(){
		Logging.debug("Adios");
	}

	/* Invoked from native code */
	private void device_connected(String btaddr) {
		cb.new_device_connected(new HDPDevice(btaddr, this));
	}

	/* Invoked from native code */
	private void device_disconected(String btaddr) {
		cb.device_disconected(new HDPDevice(btaddr, this));
	}

	/* Invoked from native code */
	private void dc_connected(long dchannel, int mdlid, String btaddr) {
		try {
			HDPDevice dev = new HDPDevice(btaddr, this);
			HDPDataChannel dc = new HDPDataChannel(dchannel, mdlid, dev);
			cb.dc_connected(dc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int read(long dc, byte[] b, int offset, int length) throws IOException  {
		if (b == null) {
            throw new NullPointerException("byte array is null");
        }
        if ((offset | length) < 0 || length > b.length - offset) {
            throw new ArrayIndexOutOfBoundsException("invalid offset or length");
        }
        return HDPread(peer, dc, b, offset, length);
	}

	public void write(long dc, byte[] b, int offset, int count) throws IOException {
		if (b == null) {
            throw new NullPointerException("buffer is null");
        }
        if ((offset | count) < 0 || count > b.length - offset) {
            throw new IndexOutOfBoundsException("invalid offset or length");
        }

        HDPwrite(peer, dc, b, offset, count);
	}

	static {
		System.loadLibrary("es_libresoft_hdp_HDPSession");
		HDPSession.initIDs();
	}
}
