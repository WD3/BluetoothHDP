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

import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Config.BloodPressureAgent;
import Config.BloodPressureMeasure;
import Config.PulseMeasure;
import android.os.Message;


import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.BloodEvent;
import es.libresoft.openhealth.BloodListener;
import es.libresoft.openhealth.PulseEvent;
import es.libresoft.openhealth.PulseListener;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.logging.ILogging;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.storage.ConfigStorageFactory;

public class ManagerShell {
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

		Logging.debug("Starting CmdManager.");
		try {
			/* uncomment next line to get HDP support for agents 
			// HDPManagerChannel chanHDP = new HDPManagerChannel();
			 uncomment next line to get TCP support for agents 
			TcpManagerChannel channelTCP = new TcpManagerChannel();
			//Set the event manager handler to get internal events from the manager thread
			InternalEventReporter.setDefaultEventManager(ieManager);
			//Set target platform to android to report measures using IPC mechanism
			MeasureReporterFactory.setDefaultMeasureReporter(new ShellMeasureReporter());

			//set ConfigStorage
			File directory = new File(".");
			ConfigStorageFactory.setDefaultConfigStorage(new ShellConfigStorage(directory.getCanonicalPath()));
			
			 Start TCP server 
			channelTCP.start();

			Logging.debug("Push any key to exit");
			System.in.read();

			//chanHDP.finish();
			channelTCP.finish();*/
			MeasureDataDecoder.setBloodListener(new BloodListener(){

				@Override
				public void getMeasure(BloodEvent event) {
					// TODO Auto-generated method stub
					System.out.println("类型："+((BloodPressureMeasure)event.getMeasures()).getType());					
					System.out.println("单位："+((BloodPressureMeasure)event.getMeasures()).getUnit());
					System.out.println("高压："+((BloodPressureMeasure)event.getMeasures()).getHPressure());
					System.out.println("低压："+((BloodPressureMeasure)event.getMeasures()).getLPressure());
					System.out.println("平均压："+((BloodPressureMeasure)event.getMeasures()).getAPressure());
					System.out.println("时间："+((BloodPressureMeasure)event.getMeasures()).getDate());
				}
				
			});
			MeasureDataDecoder.setPulseListener(new PulseListener(){

				@Override
				public void getMeasure(PulseEvent event) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			TCPManager tcpManager = new TCPManager(9999);
			tcpManager.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
