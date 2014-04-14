/*
Copyright (C) 2012 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jorge Fernandez-Gonzalez <jfernandez@libresoft.es>
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

package ieee_11073.part_20601.phd.channel.hdp;

import ieee_11073.part_20601.phd.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.bluetooth.BluetoothHealthCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.widget.Toast;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.logging.Logging;

public class HDPManagerChannel {
	private static String TAG = "HDPManagerChannel";
	private static final String srvDescName = "Libresoft 11073-20601 Manager";

	private static UUID HDP_SOURCE = UUID.fromString("00001401-0000-1000-8000-00805f9b34fb");

	// Use the appropriate IEEE 11073 data types based on the devices used.
	// Below are some examples.  Refer to relevant Bluetooth HDP specifications for detail.
	//     0x1007 - blood pressure meter
	//     0x1008 - body thermometer
	//     0x100F - body weight scale
	private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE_PULSE_OXIMETER = 0x1004;
	private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE_BLOOD_PRESSURE_MONITOR = 0x1007;
	private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_THERMOMETER = 0x1008;
	private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_WEIGHT_SCALE = 0x100F;
	private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE_GLUCOSE_METER = 0x1011;

	private Context context;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothHealth mBluetoothHealth;

	private Vector<BluetoothHealthAppConfiguration> mHealthAppsConfigs;

	public HDPManagerChannel(Context context) {
		this.context = context;
	}

	public void reloadAgents() {
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
			HDPManagedAgents.getInstance().freeAllResources();
			Set<BluetoothDevice> btDevices = mBluetoothAdapter.getBondedDevices();
			for (BluetoothDevice device: btDevices){
				checkHDPProfile(device);
			}
		} else {
			Toast.makeText(context, "bluetooth_not_available", Toast.LENGTH_LONG).show();
			return;
		}
	}

	public void start() {
		// Check for Bluetooth availability on the Android platform.
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Ensures user has turned on Bluetooth on the Android device.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			// Bluetooth adapter isn't available.  The client of the service is supposed to
			// verify that it is available and activate before invoking this service.
			Toast.makeText(context, "bluetooth_not_available", Toast.LENGTH_LONG).show();
			return;
		}

		if (!mBluetoothAdapter.getProfileProxy(this.context, mBluetoothServiceListener,
				BluetoothProfile.HEALTH)) {
			Toast.makeText(this.context, "bluetooth_health_profile_not_available",
					Toast.LENGTH_LONG).show();
			return;
		}

		reloadAgents();

		mHealthAppsConfigs = new Vector<BluetoothHealthAppConfiguration>();
		registerAllApplications();
	}

	public void finish() {
		//Destroying existing channels
		//Destroying existing applications on DBUS
		HDPManagedAgents.getInstance().freeAllResources();
		if (mHealthAppsConfigs == null)
			return;
		unregisterAllApplications();
		mHealthAppsConfigs.clear();
	}

	// Callbacks to handle connection set up and disconnection clean up.
	private final BluetoothProfile.ServiceListener mBluetoothServiceListener =
		new BluetoothProfile.ServiceListener() {
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = (BluetoothHealth) proxy;
				Logging.debug(TAG + " - onServiceConnected to profile: " + profile);
			}
		}

		public void onServiceDisconnected(int profile) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = null;
			}
		}
	};

	private final BluetoothHealthCallback mHealthCallback = new BluetoothHealthCallback() {
		// Callback to handle application registration and unregistration events.  The service
		// passes the status back to the UI client.
		public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config,
				int status) {
			if (status == BluetoothHealth.APP_CONFIG_REGISTRATION_FAILURE) {
				Logging.error(TAG + " - An error has occurred while unregistering the application.");
			} else if (status == BluetoothHealth.APP_CONFIG_REGISTRATION_SUCCESS) {
				mHealthAppsConfigs.add(config);
				Logging.debug(TAG + " - Application successfully registered.");
			} else if (status == BluetoothHealth.APP_CONFIG_UNREGISTRATION_FAILURE) {
				Logging.error(TAG + " - A failure has occurred while unregistering the application.");
			} else if (status == BluetoothHealth.APP_CONFIG_UNREGISTRATION_SUCCESS) {
				Logging.debug(TAG + " - Application successfully unregistered.");
			}
		}

		// Callback to handle channel connection state changes.
		// Note that the logic of the state machine may need to be modified based on the HDP device.
		// When the HDP device is connected, the received file descriptor is passed to the
		// ReadThread to read the content.
		public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config,
				BluetoothDevice device, int prevState, int newState, ParcelFileDescriptor fd,
				int channelId) {
			Logging.debug(TAG + " - " + String.format("prevState\t%d ----------> newState\t%d",
					prevState, newState));
			if ((prevState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED &&
					newState == BluetoothHealth.STATE_CHANNEL_CONNECTED) ||
					(prevState == BluetoothHealth.STATE_CHANNEL_CONNECTING &&
							newState == BluetoothHealth.STATE_CHANNEL_CONNECTED)) {
				for (BluetoothHealthAppConfiguration mHealthAppConfig: mHealthAppsConfigs){
					if (config.equals(mHealthAppConfig)) {
						Channel channel;
						try {
							Agent agent = HDPManagerChannel.getAgent(device);
							channel = new HDPChannel(fd, HDPManagerChannel.this, channelId, agent);
							agent.addChannel(channel);
							Logging.debug(TAG + "- HDP channel connected.");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (prevState == BluetoothHealth.STATE_CHANNEL_CONNECTING &&
					newState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED) {
				Looper.prepare();
				Toast.makeText(HDPManagerChannel.this.context,
						"An error has occurred while connecting. Please, try again",
						Toast.LENGTH_LONG).show();
				Looper.loop();
			} else if (newState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED) {
				Logging.debug(TAG + " - HDP channel disconnected.");
			}
		}
	};

	private static Agent getAgent(BluetoothDevice mDevice){
		String description = null;

		ArrayList<Agent> agents = HDPManagedAgents.getInstance().getAgents();
		for (Agent agent: agents){
			description = mDevice.getAddress() + "/ " + mDevice.getBluetoothClass().getDeviceClass();
			if (agent.getTransportDesc().equals(description))
				return agent;
		}
		return null;
	}

	private void checkHDPProfile(BluetoothDevice device){
		try {
			/*
			 * Check for HDP Source profile (0x1401)
			 */
			Method m = device.getClass().getMethod("getUuids");
			ParcelUuid[] uuids = (ParcelUuid[])m.invoke(device);

			for (int i = 0; i < uuids.length; i++){
				if (uuids[i].getUuid().equals(HDP_SOURCE)){
					Logging.debug(TAG + " - The device " + device.getAddress() + " contains HDP Profile");

					/*
					 * The description of the agent will be on the format MAC/feature
					 * being feature the number of the corresponding profile (IEEE 11073)
					 * e.g. 4100 for the pulsioximeter
					 * */
					String description = device.getAddress() + "/ " + device.getBluetoothClass().getDeviceClass();
					Logging.debug(TAG + " - Creating agent with description: " + description);

					Agent a = new Agent(description);
					HDPManagedAgents.getInstance().addAgent(a);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void registerAllApplications() {
		registerApp(HEALTH_PROFILE_SOURCE_DATA_TYPE_PULSE_OXIMETER);
		registerApp(HEALTH_PROFILE_SOURCE_DATA_TYPE_BLOOD_PRESSURE_MONITOR);
		registerApp(HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_THERMOMETER);
		registerApp(HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_WEIGHT_SCALE);
		registerApp(HEALTH_PROFILE_SOURCE_DATA_TYPE_GLUCOSE_METER);
	}

	//Register health application through the Bluetooth Health API.
	private void registerApp(int dataType) {
		mBluetoothHealth.registerSinkAppConfiguration(srvDescName, dataType, mHealthCallback);
	}

	private void unregisterAllApplications() {
		for (BluetoothHealthAppConfiguration aux: mHealthAppsConfigs){
			mBluetoothHealth.unregisterAppConfiguration(aux);
		}
	}

	// Unregister health application through the Bluetooth Health API.
	private void unregisterApp(BluetoothHealthAppConfiguration mHealthAppConfig) {
		mBluetoothHealth.unregisterAppConfiguration(mHealthAppConfig);
	}

	// Connect channel through the Bluetooth Health API.
	public void connect(Agent a) {
		BluetoothHealthAppConfiguration mHealthAppConfig = null;
		BluetoothDevice mDevice = null;

		Logging.debug(TAG + " - Connecting HDP Channel...");
		String transportDesc = a.getTransportDesc();
		String mDeviceMAC = transportDesc.substring(0, transportDesc.indexOf("/"));
		mDevice = mBluetoothAdapter.getRemoteDevice(mDeviceMAC);

		int deviceClass = mDevice.getBluetoothClass().getDeviceClass();
		int deviceDataType = getDeviceDataType(deviceClass);

		if (deviceDataType == -1){
			Looper.prepare();
			Toast.makeText(HDPManagerChannel.this.context,
					"Device not supported or unknown bluetooth class device.",
					Toast.LENGTH_LONG).show();
			Looper.loop();
			return;
		}else if (deviceClass == BluetoothClass.Device.Major.UNCATEGORIZED){
			// This case is introduced for certification purposes (Stollman board):
			Logging.debug(TAG + " - BluetoothClass.Device.Major.UNCATEGORIZED");
			for (BluetoothHealthAppConfiguration aux: mHealthAppsConfigs){
				if (aux.getDataType() == deviceDataType)
					mHealthAppConfig = aux;
			}

			if (mHealthAppConfig != null){
				mBluetoothHealth.connectChannelToSource(mDevice, mHealthAppConfig);
			}
		}else if (deviceDataType > 0){
			for (BluetoothHealthAppConfiguration aux: mHealthAppsConfigs){
				if (aux.getDataType() == deviceDataType)
					mHealthAppConfig = aux;
			}

			if (mHealthAppConfig != null)
				mBluetoothHealth.connectChannelToSource(mDevice, mHealthAppConfig);
		}
	}

	// Disconnect channel through the Bluetooth Health API.
	public void disconnect(Agent a, int channelId) {
		BluetoothHealthAppConfiguration mHealthAppConfig = null;

		Logging.debug(TAG + " - Disconnecting HDP Channel...");

		String transportDesc = a.getTransportDesc();
		String mDeviceMAC = transportDesc.substring(0, transportDesc.indexOf("/"));
		BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(mDeviceMAC);

		int deviceClass = mDevice.getBluetoothClass().getDeviceClass();
		int deviceDataType = getDeviceDataType(deviceClass);

		for (BluetoothHealthAppConfiguration aux: mHealthAppsConfigs){
			if (aux.getDataType() == deviceDataType)
				mHealthAppConfig = aux;
		}

		mBluetoothHealth.disconnectChannel(mDevice, mHealthAppConfig, channelId);
		//HDPManagedAgents.getInstance().delAgent(a);
	}

	/**
	 * Returns de device data type (-1 if the device data type is not supported).
	 */
	private int getDeviceDataType(int deviceClass){
		int deviceDataType = -1;

		switch(deviceClass){
			case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_BLOOD_PRESSURE supported");
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_BLOOD_PRESSURE_MONITOR;
				break;
			case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_PULSE_OXIMETER supported");
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_PULSE_OXIMETER;
				break;
			case BluetoothClass.Device.HEALTH_THERMOMETER:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_THERMOMETER supported");
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_THERMOMETER;
				break;
			case BluetoothClass.Device.HEALTH_WEIGHING:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_WEIGHIN supported");
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_BODY_WEIGHT_SCALE;
				break;
			case BluetoothClass.Device.HEALTH_GLUCOSE:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_GLUCOSE supported");
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_GLUCOSE_METER;
				break;
			case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_DATA_DISPLAY not supported" );
				deviceDataType = -1;
				break;
			case BluetoothClass.Device.HEALTH_PULSE_RATE:
				Logging.debug(TAG + " - BluetoothClass.Device.HEALTH_PULSE_RATE not supported");
				deviceDataType = -1;
				break;
			case BluetoothClass.Device.Major.UNCATEGORIZED:
				// This case is introduced for certification purposes (stollman board):
				Logging.debug(TAG + " - BluetoothClass.Device.Major.UNCATEGORIZED");
				//deviceDataType = BluetoothClass.Device.Major.UNCATEGORIZED;
				// To certification for pulse oximeters devices:
				deviceDataType = HEALTH_PROFILE_SOURCE_DATA_TYPE_PULSE_OXIMETER;
			default:
				Logging.debug(TAG + " - Unknown BluetoothClass.Device = " + deviceClass + " not supported");
				deviceDataType = -1;
				break;
		}
		return deviceDataType;
	}
}
