package es.libresoft.openhealth;

import ieee_11073.part_20601.phd.channel.tcp.TcpAgentChannel;

import com.example.bluetooth.health.BluetoothHDPService;

import es.libresoft.openhealth.android.IEventAgent;
import es.libresoft.openhealth.events.InternalEventReporter;

public class TCPAgent {
	private Specialization spec;
	private String host;
	private int port;
	private String unique;
	private TcpAgentChannel channelTCP;
	
	public TCPAgent(Specialization spec, String host, int port, String unique){
		this.spec = spec;
		this.host = host;
		this.port = port;
		this.unique = unique;
	}
	public void start(){
		synchronized(unique){
			/* uncomment next line to get HDP support for agents */
			// HDPManagerChannel chanHDP = new HDPManagerChannel();
			/* uncomment next line to get TCP support for agents */
			channelTCP = new TcpAgentChannel(spec, host, port);
			//Set the event manager handler to get internal events from the manager thread
			InternalEventReporter.setDefaultEventManager(new IEventAgent());
			
			//Set target platform to android to report measures using IPC mechanism
			//MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.SHELL);

			/* Start TCP server */
			channelTCP.connect();

//			System.out.println("Push any key to exit");
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
//			System.in.read();

			//chanHDP.finish();
		}		
	}
	public void finish(){
		channelTCP.finish();
	}
}
