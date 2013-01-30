package org.celstec.arlearn2.jdo.manager;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.IOSDevicesRegistryJDO;

import com.google.appengine.api.blobstore.BlobKey;

public class IOSDevicesRegistryManager {

	public static void addDevice(APNDeviceDescription deviceDes) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		IOSDevicesRegistryJDO jdo = new IOSDevicesRegistryJDO();
		jdo.setAccount(deviceDes.getAccount());
		jdo.setDeviceToken(deviceDes.getDeviceToken());
		jdo.setDeviceUniqueIdentifier(deviceDes.getDeviceUniqueIdentifier());
		try {
			pm.makePersistent(jdo);
		} finally {
			pm.close();
		}
	}
	
	public static String[] getDeviceTokens(String account) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(IOSDevicesRegistryJDO.class);
			query.setFilter("account == accountParam");
			query.declareParameters("String accountParam");
			List<IOSDevicesRegistryJDO> list = (List<IOSDevicesRegistryJDO>) query.execute(account);
			String[] result = new String[list.size()];
			int i = 0;
			for (IOSDevicesRegistryJDO jdo : list) {
				result[i++] = jdo.getDeviceToken();
			}
			return result;
		}finally {
			pm.close();
		}
		
	}
	
}
