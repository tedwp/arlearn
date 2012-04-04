package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class UserJDO extends RunClass {

	@Persistent
    private String teamId;
	
	@Persistent
    private String name;
	
	@Persistent
    private String email;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setId() {
		if (getEmail() != null && getRunId() !=null) {
			this.id = KeyFactory.createKey(UserJDO.class.getSimpleName(), generateId(getEmail(), getRunId()));
		}
	}
	
	public static String generateId(String email, Long runId) {
		return runId +":"+email;
	}
}
