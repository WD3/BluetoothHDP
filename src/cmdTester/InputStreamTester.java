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

package cmdTester;

public class InputStreamTester {

	/**
	 * @param args
	 */
	public static String file = "test.txt";

	public static void main(String[] args) {
		/*
		try {
			HDPInputStream in1 = new HDPInputStream (
					new FileInputStream (
							new File (file)));

			int r;
			for (int i = 0; i < 34; i++) {
				r = in1.read();
				if (r > 0)
					System.out.print((char)r );
				else
					Logging.debug("-");
			}

			HDPInputStream in2 = new HDPInputStream (
					new FileInputStream (
							new File (file)));

			byte buff[] = new byte[33];
			int offset = 0, len = buff.length;
			while (true) {
				r = in2.read(buff, offset, len);
				if (r < 0) {
					Logging.debug("Fin con offset="+offset);
					break;
				}
				offset = offset + r;
				len = buff.length - offset;
				Logging.debug("offset=" + offset + ", len=" + len);
			}
			for (int i = 0; i < 33; i++)
				System.out.print((char)buff[i]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

}
