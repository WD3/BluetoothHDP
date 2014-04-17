package com.pku.wireless;

import com.example.bluetooth.health.BluetoothHDPService;

import android.os.ParcelFileDescriptor;
import ieee_11073.part_20601.phd.channel.bluetooth.hq.HDPChannel;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.android.AndroidILogging;
import es.libresoft.openhealth.android.AndroidMeasureReporter;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.logging.Logging;

public class HDPManager {
	private ParcelFileDescriptor fd;
	private String unique;

	public HDPManager(ParcelFileDescriptor fd, String unique) {
		this.fd = fd;
		this.unique = unique;
	}

	public void start() {
		synchronized (unique) {
			Logging.setDefaultLogGenerator(new AndroidILogging()); // 打印信息
			InternalEventReporter.setDefaultEventManager(new AndroidEventManager()); // 重写内部接口，状态机运行为Manager端，并对不同状态作出响应

			HDPChannel chnl;
			try {
				chnl = new HDPChannel(fd);
				Agent a = new Agent("");
				a.addChannel(chnl);
				// agents.addAgent(a);
				MeasureReporterFactory
						.setDefaultMeasureReporter(new AndroidMeasureReporter());
				// ConfigStorageFactory.setDefaultConfigStorage(new
				// AndroidConfigStorage(BluetoothHDPService.this.getApplicationContext()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
