package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;

public class LocationUpdateConfig extends Bean{
	
	private Long delay;
	private Long timeBetweenUpdates;
	private String role;
	private String scope;
	
	public Long getDelay() {
		return delay;
	}
	public void setDelay(Long delay) {
		this.delay = delay;
	}
	public Long getTimeBetweenUpdates() {
		return timeBetweenUpdates;
	}
	public void setTimeBetweenUpdates(Long timeBetweenUpdates) {
		this.timeBetweenUpdates = timeBetweenUpdates;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public boolean equals(Object obj) {
		LocationUpdateConfig other = (LocationUpdateConfig ) obj;
		
		return super.equals(obj) && 
			nullSafeEquals(getDelay(), other.getDelay()) && 	
			nullSafeEquals(getTimeBetweenUpdates(), other.getTimeBetweenUpdates()) && 
			nullSafeEquals(getRole(), other.getRole()) && 
			nullSafeEquals(getScope(), other.getScope()); 

	}

}
