/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

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
package ieee_11073.part_20601.phd.channel.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Config.BloodPressureAgent;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.Specialization;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;

//import android.util.Log;

public class TcpAgentChannel{
	private Socket s;
	private Agent a;
	private String status = "";
	private Specialization spec;
	private String host;
	private int port;
	
	public TcpAgentChannel(Specialization spec, String host, int port){
		this.spec = spec;
		this.host = host;
		this.port = port;
	}
	
	public void connect(){
		try {
//			s = new Socket("127.0.0.1", 9999);
			s = new Socket(host, port);
			System.out.println("Server connected on " + s.getInetAddress() + ":" + s.getLocalPort());
			TCPChannel chnl = new TCPChannel (s,null,a);
			a = new Agent(s.getLocalSocketAddress().toString());
			a.setSpecialization(spec);
			a.addChannel(chnl);
			a.sendEvent(new Event(EventType.REQ_ASSOC));
		}
		catch (IOException e) {
			if (s!=null && s.isClosed()){
				status = "Ok";
			}else{
				status = "Error";
				System.out.println("Error: Can't connecte to server." + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = "Unexpected error";
			System.out.println("Unexpected error: " + e.getMessage());
		}
	}

	public void finish() {
		// TODO Auto-generated method stub
		if(s==null){
			System.out.println("agent service has already exited");	
			return;
		}
		System.out.println("Closing agent");
		try {
			s.close();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		System.out.println("agent service exiting..." + status);
		a.freeResources();
	}
}