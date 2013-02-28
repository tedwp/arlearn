/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
