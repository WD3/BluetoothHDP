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
import java.io.InputStream;

public class HDPInputStream extends InputStream{

		private HDPSession session;
		/*Data channel descriptor*/
		private long dcd;

		protected byte buff[];
		protected int coun;
		protected int pos;

		private static final int L2CAP_DEFAULT_MTU = 672;

		public HDPInputStream(HDPSession session, long dc){
			this.session = session;
			this.dcd = dc;
			buff = new byte[L2CAP_DEFAULT_MTU];
			coun = 0;
			pos = 0;
		}

		@Override
		public int read() throws IOException {
			if (pos >= coun) {
				int ret;
				/* get next packet */
				ret = session.read(dcd, buff, 0, L2CAP_DEFAULT_MTU);
				if (ret <= 0)
					return -1;
				coun = ret;
				pos = 0;
				//Logging.debug("readed: " + coun + ", pos: " + pos);
			}

			//Logging.debug("coun: " + coun + ", pos: " + pos);

			return (int)buff[pos++] & 0xff;
		}

		@Override
		public int read(byte[] b, int offset, int length) throws IOException {
			int r;
			if (pos >= coun) {
				/* get next packet */
				r = session.read(dcd, buff, 0, L2CAP_DEFAULT_MTU);
				if (r <= 0)
					return r;
				coun = r;
				pos = 0;
			}

			r = coun - pos;
			if (r >= length) {
				System.arraycopy(buff, pos, b, offset, length);
				pos = pos + length;
				return length;
			}

			System.arraycopy(buff, pos, b, offset, r);
			pos = coun;
			return r;
		}

		@Override
		public int available() throws IOException {
			return coun - pos;
		}

		@Override
		public boolean markSupported() {
			return false;
		}
}
