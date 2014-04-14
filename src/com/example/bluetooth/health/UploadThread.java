package com.example.bluetooth.health;

import ieee_11073.part_20601.phd.channel.tcp.TcpAgentChannel;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Config.BloodPressureAgent;
import Config.ECGAgent;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.TCPAgent;
import es.libresoft.openhealth.android.IEventAgent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.ILogging;
import es.libresoft.openhealth.logging.Logging;

public class UploadThread implements Runnable{
	private String unique;
	
	public UploadThread(String unique){
		this.unique = unique;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			/* uncomment next line to get HDP support for agents 
			// HDPManagerChannel chanHDP = new HDPManagerChannel();
			 uncomment next line to get TCP support for agents 
			TcpAgentChannel channelTCP = new TcpAgentChannel(BluetoothHDPService.bloodPressureAgent);
			//Set the event manager handler to get internal events from the manager thread
			InternalEventReporter.setDefaultEventManager(new IEventAgent());
			
			//Set target platform to android to report measures using IPC mechanism
			//MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.SHELL);

			 Start TCP server 
			channelTCP.connect();

			System.out.println("Push any key to exit");
			Thread.sleep(10000);
//			System.in.read();

			//chanHDP.finish();
			channelTCP.finish();*/
			TCPAgent tcpAgent = new TCPAgent(BluetoothHDPService.bloodPressureAgent,"162.105.76.69",9999,unique);
			tcpAgent.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


