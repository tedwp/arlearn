package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class UserJDO extends RunClass {

	@Persistent
    private String teamId;
	
	@Persistent
    private String name;
	
	@Persistent
    private String email;

	@Persistent
	private Text payload;
	
	@Persistent
	private Long lastModificationDate;
	
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
	
	public Text getPayload() {
		return payload;
	}

	public void setPayload(Text payload) {
		this.payload = payload;
	}
	
	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
}
