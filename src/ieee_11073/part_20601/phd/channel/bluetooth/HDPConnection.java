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

import ieee_11073.part_20601.phd.channel.Channel;
import es.libresoft.hdp.HDPDevice;
import es.libresoft.openhealth.Agent;

public class HDPConnection {
	private HDPDevice device;
	private Agent agent;

	public HDPConnection (HDPDevice device) {
		this.device = device;
		this.agent = new Agent(device.toString());
	}

	public HDPDevice getHDPDevice () {
		return this.device;
	}

	public void addDataChannel (Channel chan) {
		agent.addChannel(chan);
	}

	public void delDataChannel (Channel chan) {
		agent.delChannel(chan);
	}

	public void freeResources() {
		agent.freeResources();
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof HDPConnection)
			return device.equals(((HDPConnection)o).getHDPDevice());
		return false;
	}

}
