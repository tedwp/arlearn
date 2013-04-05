package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class AccountJDO {
	
	public final static int FBCLIENT = 1;
	public final static int GOOGLECLIENT = 2;
	public final static int LINKEDINCLIENT = 3;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key uniqueId;
	
	@Persistent
	private String localId;
	
	@Persistent
	private Integer accountType;
	
	@Persistent
	private String email;
	
	@Persistent
	private String name;
	
	@Persistent
	private String given_name;
	
	@Persistent
	private String family_name;
	
	@Persistent
	private String picture;

	public String getUniqueId() {
		return uniqueId.getName();
	}

	public void setUniqueId() {
		this.uniqueId = KeyFactory.createKey(AccountJDO.class.getSimpleName(), getAccountType()+":"+getLocalId());

		
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}
