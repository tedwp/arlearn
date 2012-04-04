package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class TeamJDO  extends RunClass{

	@Persistent
    private String name;

	public Key getTeamId() {
		return id;
	}
	
	public String getTeamIdString() {
		return id.getName();
	}

	public void setTeamId(String teamId) {
		if (teamId != null) 
			setTeamId(KeyFactory.createKey(TeamJDO.class.getSimpleName(), teamId));
	}
	
	public void setTeamId(Key teamId) {
		this.id = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
