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
package org.celstec.arlearn2.beans.run;

import java.util.List;

public class User extends RunBean{

	private String teamId;
	private String email;
	private String fullEmail;
	private String name;

	private List<String> roles;

	private Double lng;
	private Double lat;
	
	private Long gameId;
	private Long lastModificationDateGame;
	
	public User() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		User other = (User) obj;
		return super.equals(obj) && 
			nullSafeEquals(getTeamId(), other.getTeamId()) &&
			nullSafeEquals(getEmail(), other.getEmail()) &&
			nullSafeEquals(getFullEmail(), other.getFullEmail()) &&
			nullSafeEquals(getName(), other.getName()) &&
			nullSafeEquals(getRoles(), other.getRoles()) ; 
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullEmail() {
		return fullEmail;
	}

	public void setFullEmail(String fullEmail) {
		this.fullEmail = fullEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public static String normalizeEmail(String mail) {
		if (mail == null) return null;
		int posAt = mail.indexOf("@");
		if (posAt != -1) {
			mail = mail.substring(0, posAt);
		}
		return mail.replace(".", "").toLowerCase();
	}
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	public Long getLastModificationDateGame() {
		return lastModificationDateGame;
	}

	public void setLastModificationDateGame(Long lastModificationDateGame) {
		this.lastModificationDateGame = lastModificationDateGame;
	}
	
}
