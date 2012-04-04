package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class RunJDO extends RunClass{

	@Persistent
	private Long gameId;
	
	@Persistent
	private String title;
	
	@Persistent
    private String owner;
	
	@Persistent
	private Long startTime;

	@Persistent
	private Long serverCreationTime;
	
	@Persistent
	private String gameOverDependsOn;

	public Long getRunId() {
		return id.getId();
	}
	
	public void setRunId(Long runId) {
		if (runId != null) 
			setRunId(KeyFactory.createKey(RunJDO.class.getSimpleName(), runId));
	}
	
	public void setRunId(Key runId) {
		this.id = runId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getServerCreationTime() {
		return serverCreationTime;
	}

	public void setServerCreationTime(Long serverCreationTime) {
		this.serverCreationTime = serverCreationTime;
	}

	public String getGameOverDependsOn() {
		return gameOverDependsOn;
	}

	public void setGameOverDependsOn(String gameOverDependsOn) {
		this.gameOverDependsOn = gameOverDependsOn;
	}

	
}
