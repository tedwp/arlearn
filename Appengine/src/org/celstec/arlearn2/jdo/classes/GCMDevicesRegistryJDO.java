package org.celstec.arlearn2.jdo.classes;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class GCMDevicesRegistryJDO {

	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key id;
	
	@Persistent
	private String account;
	
	@Persistent
	private String registrationId;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		if (registrationId != null) 
			this.id = KeyFactory.createKey(GCMDevicesRegistryJDO.class.getSimpleName(), registrationId);
		this.registrationId = registrationId;
	}
	
	
}

