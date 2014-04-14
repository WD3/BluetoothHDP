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
import java.io.OutputStream;

public class HDPOutputStream extends OutputStream {

		private HDPSession session;
		/*Data channel descriptor*/
		private long dcd;

		HDPOutputStream(HDPSession session, long dc){
			this.session = session;
			this.dcd = dc;
		}

		@Override
		public void write(int oneByte) throws IOException {
			byte b[] = new byte[1];
	        b[0] = (byte)oneByte;
			session.write(dcd, b, 0, 1);
		}

		@Override
		public void write(byte[] b, int offset, int count) throws IOException {
			session.write(dcd, b, offset, count);
		}
}
