package org.celstec.arlearn2.beans.run;

import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;


public class User extends RunBean{

	private String teamId;
	private String email;
	private String name;

	private List<String> roles;

	private Double lng;
	private Double lat;
	
	public User() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		User other = (User) obj;
		return super.equals(obj) && 
			nullSafeEquals(getTeamId(), other.getTeamId()) &&
			nullSafeEquals(getEmail(), other.getEmail()) &&
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
	
	
}