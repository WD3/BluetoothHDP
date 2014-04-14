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
package ieee_11073.part_20601.phd.channel.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.logging.Logging;

//import android.util.Log;

public class TcpManagerChannel extends Thread {
	private boolean finish = false;
	private ServerSocket ss;
	private TCPManagedAgents agents = new TCPManagedAgents();
	private int port;

	public TcpManagerChannel(int port){
		this.port = port;
	}
	public void run() {
		String status="";
		try {
			ss = new ServerSocket (9999);
			Logging.debug("Server attached on " + ss.getInetAddress() + ":" + ss.getLocalPort());
			Logging.debug("Waiting for clients...");
			while(!this.finish){
				Socket s = ss.accept();
				Agent a = new Agent(s.getRemoteSocketAddress().toString());
				TCPChannel chnl = new TCPChannel (s, this, a);
				a.addChannel(chnl);
				agents.addAgent(a);
			}
			if (!ss.isClosed()) //Finishing in good way
				ss.close();
		} catch (IOException e) {
			if (ss!=null && ss.isClosed()){
				status = "Ok";
			}else{
				status = "Error";
				Logging.debug("Error: Can't create a server socket in 9999." + e.getMessage());
			}
		} catch (Exception e) {
			status = "Unexpected error";
			Logging.debug("Unexpected error: " + e.getMessage());
		} finally {
			Logging.debug("manager service exiting..." + status);
		}
		agents.freeAllResources();
	}

	public void finish() {
		this.finish = true;
		Logging.debug("Closing manager service...");
		try {
			ss.close();
		} catch (IOException e) {
			Logging.debug("Error: " + e.getMessage());
		}
	}

	public void disconnectAgent(Agent agent) {
		agents.delAgent(agent);
	}
}