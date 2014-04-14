package cmdTester;

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

import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.StorageException;
import es.libresoft.openhealth.storage.StorageNotFoundException;
import es.libresoft.openhealth.utils.ASN1_Tools;

public class ShellConfigStorage implements ConfigStorage {

	private String rootDir;

	public ShellConfigStorage(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public Collection<ConfigObject> recover(byte[] sysId, DeviceConfig config) throws StorageNotFoundException {
		ArrayList<ConfigObject> knowconf = new ArrayList<ConfigObject>();
		try {
			String sysid;
			sysid = ASN1_Tools.getHexString(sysId);
			File base_dir = new File(rootDir);
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
			File base_dir = new File(rootDir);
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
			File base_dir = new File(rootDir);
			File dir_file = new File(base_dir.getAbsolutePath() + "/" + sysid + "/" + config.getPhdId());
			delete(dir_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
