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

import java.util.ArrayList;

import ieee_11073.part_10101.Nomenclature;
import es.libresoft.hdp.Feature;
import es.libresoft.hdp.FeatureGroup;
import es.libresoft.hdp.HDPCallbacks;
import es.libresoft.hdp.HDPConfig;
import es.libresoft.hdp.HDPDataChannel;
import es.libresoft.hdp.HDPDevice;
import es.libresoft.hdp.HDPSession;
import es.libresoft.openhealth.logging.Logging;

public class TestJNI {

	public static ArrayList list = new ArrayList<HDPDataChannel>();

	public static HDPCallbacks callbacks = new HDPCallbacks(){
		@Override
		public void dc_connected(HDPDataChannel dc) {
			Logging.debug("callback: dc_connected");
			list.add(dc);
		}

		@Override
		public void dc_deleted(HDPDataChannel dc) {
			Logging.debug("callback: dc_deleted");
		}

		@Override
		public void device_disconected(HDPDevice dev) {
			Logging.debug("callback: device_disconected");
		}

		@Override
		public void new_device_connected(HDPDevice dev) {
			Logging.debug("callback: new_device_connected");
		}

	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Feature[] fs = new Feature [] {
				new Feature (Nomenclature.MDC_DEV_SPEC_PROFILE_PULS_OXIM, "Pulse-oximeter"),
		};
		FeatureGroup[] fg = new FeatureGroup[] {
				new FeatureGroup(fs,FeatureGroup.SINK_ROLE),
		};
		HDPConfig conf = new HDPConfig("string1", "string2", "string3", fg);
		try {
			HDPSession hdp = new HDPSession(conf, callbacks);
			hdp.close();
			Logging.debug("Push any key to exit");
			System.in.read();
			hdp.free();
			Logging.debug("Exiting...");
			System.in.read();
			Logging.debug("Exited.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
