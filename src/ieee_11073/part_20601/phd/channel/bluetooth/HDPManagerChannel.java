/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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

package ieee_11073.part_20601.phd.channel.bluetooth;

import ieee_11073.part_10101.Nomenclature;
import es.libresoft.hdp.Feature;
import es.libresoft.hdp.FeatureGroup;
import es.libresoft.hdp.HDPCallbacks;
import es.libresoft.hdp.HDPConfig;
import es.libresoft.hdp.HDPDataChannel;
import es.libresoft.hdp.HDPDevice;
import es.libresoft.hdp.HDPSession;
import es.libresoft.openhealth.logging.Logging;

public class HDPManagerChannel {

	private static final String srvName = "Libresoft Manager";
	private static final String srvDescName = "Libresoft 11073-20601 Manager";
	private static final String provName = "Libresoft";

	private HDPSession hdps;
	private HDPManagedDevices devices;
	private HDPConfig config;
	private HDPCallbacks callbacks = new HDPCallbacks () {

		@Override
		public void dc_connected(HDPDataChannel dc) {
			Logging.debug("JAVA_DATA_CHANNEL_CREATE on " + dc.getDevice().getBtAddress() + " mdlid " + dc.getChannelId());

			HDPConnection con = devices.getHDPConnection(dc.getDevice());
			con.addDataChannel(dc);
		}

		@Override
		public void dc_deleted(HDPDataChannel dc) {
			Logging.debug("JAVA_DATA_CHANNEL_DELETED");
			/*
			HDPConnection con = devices.getHDPConnection(dc.getDevice());
			con.delDataChannel(dc);
			*/
		}

		@Override
		public void device_disconected(HDPDevice dev) {
			Logging.debug("JAVA_DISCONNECTED");

			HDPConnection con = devices.getHDPConnection(dev);
			if (con == null) {
				Logging.debug("TODO: Revice disconnect device");
				return;
			}
			devices.delHDPDevice(con);
		}

		@Override
		public void new_device_connected(HDPDevice dev) {
			Logging.debug("JAVA_CONNECTED");
			HDPConnection con = devices.getHDPConnection(dev);
			if (con != null) {
				Logging.debug("TODO: HDP Dispositivo reconectado");
				return;
			}

			con = new HDPConnection (dev);
			devices.addHDPDevice(con);
		}

	};

	public HDPManagerChannel () throws Exception {
		Feature[] fs = new Feature [] {
				new Feature (Nomenclature.MDC_DEV_SPEC_PROFILE_PULS_OXIM, "Pulse-oximeter"),
				new Feature (Nomenclature.MDC_DEV_SPEC_PROFILE_SCALE, "Weighing-scale"),
		};
		FeatureGroup[] fg = new FeatureGroup[] {
				new FeatureGroup(fs,FeatureGroup.SINK_ROLE),
		};
		config = new HDPConfig(srvName, srvDescName, provName, fg);
		devices = new HDPManagedDevices();
		hdps = new HDPSession(config, callbacks);
	}

	public HDPManagerChannel (String sName, String sDscName, String pName, FeatureGroup[] fgrp) throws Exception {
		config = new HDPConfig (sName, sDscName, pName, fgrp );
		hdps = new HDPSession(config, callbacks);
	}

	public void finish() {
		Logging.debug("Closing HDP manager service...");
		hdps.close();
		hdps.free();
		devices.freeAllResources();
	}
}
