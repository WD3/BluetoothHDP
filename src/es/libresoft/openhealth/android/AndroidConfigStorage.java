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

package es.libresoft.openhealth.android;

import ieee_11073.part_20601.asn1.ConfigObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.bn.CoderFactory;
import org.bn.IDecoder;
import org.bn.IEncoder;

import android.content.Context;

import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.StorageException;
import es.libresoft.openhealth.storage.StorageNotFoundException;
import es.libresoft.openhealth.utils.ASN1_Tools;

public class AndroidConfigStorage implements ConfigStorage {

	private Context context;
	private static final String storage = "storage";

	public AndroidConfigStorage(Context context) {
		this.context = context;
	}

	@Override
	public Collection<ConfigObject> recover(byte[] sysId, DeviceConfig config) throws StorageNotFoundException {
		ArrayList<ConfigObject> knowconf = new ArrayList<ConfigObject>();
		try {
			String sysid;
			sysid = ASN1_Tools.getHexString(sysId);
			File base_dir = context.getDir(storage, Context.MODE_PRIVATE);
			File dir_file = new File(base_dir.getAbsolutePath() + "/" + sysid + "/" + config.getPhdId());

			File[] confs = dir_file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return (filename.endsWith(".conf"));
				}

			});

			if(confs == null)
				throw new StorageNotFoundException("Directory with the configuration does not exist");

			IDecoder decoder = CoderFactory.getInstance().newDecoder("MDER");
			for (int i = 0; i < confs.length; i++) {
				FileInputStream is = new FileInputStream(confs[i]);
				knowconf.add(decoder.decode(is, ConfigObject.class));
				is.close();
			}

		} catch (Exception e) {
			throw new StorageNotFoundException(e);
		}

		if (knowconf.size() == 0)
			throw new StorageNotFoundException("Directory with the configuration does not exist");

		return knowconf;
	}

	@Override
	public void store(byte[] sysId, DeviceConfig config, ConfigObject data) throws StorageException {
		try {
			IEncoder<ConfigObject> encoder = CoderFactory.getInstance().newEncoder("MDER");

			String sysid = ASN1_Tools.getHexString(sysId);
			File base_dir = context.getDir(storage, Context.MODE_PRIVATE);
			File dir_file = new File(base_dir.getAbsolutePath() + "/" + sysid + "/" + config.getPhdId());
			dir_file.mkdirs();

			File file = new File(dir_file.getAbsoluteFile(), data.getObj_handle().getValue().getValue() + ".conf");
			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file, false);
			encoder.encode(data, fos);
			fos.close();

		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			if (file.list().length == 0)
				file.delete();
			else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}

				if (file.list().length == 0)
					file.delete();
			}
		} else
			file.delete();
	}

	@Override
	public void delete(byte[] sysId, DeviceConfig config) {
		try {
			String sysid = ASN1_Tools.getHexString(sysId);
			File base_dir = context.getDir(storage, Context.MODE_PRIVATE);
			File dir_file = new File(base_dir.getAbsolutePath() + "/" + sysid + "/" + config.getPhdId());
			delete(dir_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
