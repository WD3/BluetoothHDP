//Hongqiao Gao
package cmdTester;

import ieee_11073.part_20601.phd.channel.bluetooth.HDPManagerChannel;
import ieee_11073.part_20601.phd.channel.tcp.TcpAgentChannel;
import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Config.BloodPressureAgent;
import Config.ECGAgent;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.logging.ILogging;
import es.libresoft.openhealth.logging.Logging;

public class AgentShell {

	
	private static InternalEventManager ieManager = new InternalEventManager(){

		@Override
		public void receivedMeasure(Agent agent, MeasureReporter mr) {
			List<Object> measures = mr.getMeasures();
			Iterator<Object> ims = measures.iterator();

			List<Object> attributes = mr.getAttributes();
			Iterator<Object> iat = attributes.iterator();

			if (!measures.isEmpty()) {
				System.out.println("Measures received from: " + agent.getId());
				while (ims.hasNext()) {
					System.out.println("" + ims.next());
				}
			}

			if (!attributes.isEmpty()) {
				System.out.println("Attributes received from: " + agent.getId());
				while (iat.hasNext()) {
					System.out.println("" + iat.next());
				}
			}
			
		}

		@Override
		public void agentChangeState(Agent agent, int stateCode,
				String stateName) {
			System.out.println("ID: " + agent.getId() + " state: " + stateName);
			if(stateName == "Operating"){
				Timer timer = new Timer();
				timer.schedule(new MyTask(agent), 2000);
			}
		}
		
		class MyTask extends TimerTask{
			private Agent agent;
			public MyTask(Agent agent){
				this.agent=agent;
			}
			@Override
			public void run() {	
				agent.sendEvent(new Event(EventType.NOTI_MEASURE));
			}
		}

		@Override
		public void agentPlugged(Agent agent) {
			System.out.println("Agent " + agent.getId() + " connected");
			
		}

		@Override
		public void agentUnplugged(Agent agent) {
			System.out.println("Agent " + agent.getId() + " disconnected");
		}

		@Override
		public void error(Agent agent, int errorCode) {
			// TODO Auto-generated method stub
			
		}

	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logging.setDefaultLogGenerator(new ILogging(){

			@Override
			public void error(String str) {
				System.err.println(str);
			}

			@Override
			public void debug(String str) {
				System.out.println(str);
			}

			@Override
			public void info(String str) {
				System.out.println(str);
			}
		});
		Logging.debug("Starting CmdAgent.");
		try {
			/* uncomment next line to get HDP support for agents */
			// HDPManagerChannel chanHDP = new HDPManagerChannel();
			/* uncomment next line to get TCP support for agents */
			TcpAgentChannel channelTCP = new TcpAgentChannel(new ECGAgent(),"162.105.76.179",9999);
			//Set the event manager handler to get internal events from the manager thread
			InternalEventReporter.setDefaultEventManager(ieManager);
			
			//Set target platform to android to report measures using IPC mechanism
			//MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.SHELL);

			/* Start TCP server */
			channelTCP.connect();

			System.out.println("Push any key to exit");
			System.in.read();

			//chanHDP.finish();
			channelTCP.finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
