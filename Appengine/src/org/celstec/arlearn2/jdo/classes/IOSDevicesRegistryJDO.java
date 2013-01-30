package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class IOSDevicesRegistryJDO {

	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key id;
	
	@Persistent
	private String account;
	
	@Persistent
	private String deviceUniqueIdentifier;
	
	@Persistent
	private String deviceToken;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDeviceUniqueIdentifier() {
		return deviceUniqueIdentifier;
	}

	public void setDeviceUniqueIdentifier(String deviceUniqueIdentifier) {
		if (deviceUniqueIdentifier != null) 
			this.id = KeyFactory.createKey(IOSDevicesRegistryJDO.class.getSimpleName(), deviceUniqueIdentifier);
		this.deviceUniqueIdentifier = deviceUniqueIdentifier;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
}
