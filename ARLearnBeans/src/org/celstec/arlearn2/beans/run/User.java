package org.celstec.arlearn2.beans.run;

import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;


public class User extends RunBean{

	private String teamId;
	private String email;
	private String fullEmail;
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
	
}
