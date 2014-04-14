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
package es.libresoft.openhealth;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.utils.FIFO;
import es.libresoft.openhealth.utils.IFIFO;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.phd.channel.Channel;
import ieee_11073.part_20601.phd.channel.VirtualChannel;

public abstract class Device {

	private static int instance_id = 0;
	private int id;
	private String transporDesc;

	public static final String MDER_DEFAULT = "MDER";

	private VirtualChannel vch;

	protected IFIFO<ApduType> inputQueue;
	protected IFIFO<ApduType> outputQueue;
	protected IFIFO<Event> eventQueue;


	public Device (String transportDescription)
	{
		transporDesc = transportDescription;
		inputQueue = new FIFO<ApduType>();
		outputQueue = new FIFO<ApduType>();
		eventQueue = new FIFO<Event>();
		vch = new VirtualChannel(inputQueue, outputQueue, eventQueue);
		id = getNewId();
		notifyDevicePlugged();
	}

	public void addChannel (Channel channel)
	{
		vch.addChannel(channel);
	}

	public void delChannel (Channel channel)
	{
		vch.delChannel(channel);
	}

	public void freeResources (){
		vch.freeChannels();
		notifyDeviceUnplugged();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return this.id;
	}

	public abstract void notifyDevicePlugged();

	public abstract void notifyDeviceUnplugged();

	public String getTransportDesc() {
		return transporDesc;
	}

	private static synchronized int getNewId() {
		return instance_id++;
	}
}
