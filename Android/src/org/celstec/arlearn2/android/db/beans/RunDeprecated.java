package org.celstec.arlearn2.android.db.beans;

import java.io.Serializable;

import android.util.Log;

public class RunDeprecated implements Serializable {

	private Long gameId;
	private Long runId;
	private String title;
	private String error;
	private Long startTime;
	
	public boolean equals(RunDeprecated other) {
		if (!nullSafeEquals(getGameId(),other.getGameId())) return false; 
		if (!nullSafeEquals(getRunId(),other.getRunId())) return false; 
		if (!nullSafeEquals(getTitle(),other.getTitle())) return false; 
		if (!nullSafeEquals(getStartTime(),other.getStartTime())) return false; 
		
		return true;
	}
	
	private boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		return o1.equals(o2);
	}
	
	public RunDeprecated(){
		
	}
	
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	         
	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	

}

