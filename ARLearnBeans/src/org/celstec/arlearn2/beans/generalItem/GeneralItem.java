package org.celstec.arlearn2.beans.generalItem;

import java.util.List;

import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.game.GameBean;

public class GeneralItem extends GameBean{
	
	private Long id;

	private String scope;
	
	private String name;

	private String description;

	private Dependency dependsOn;

	private String type;
	
	private Integer radius;
	
	private Long showAtTimeStamp;

	private Double lng;

	private Double lat;
	
	private String iconUrl;
	
	private List<String> roles;
	
	@Override
	public boolean equals(Object obj) {
		GeneralItem other = (GeneralItem ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getId(), other.getId()) &&
			nullSafeEquals(getScope(), other.getScope()) &&
			nullSafeEquals(getName(), other.getName()) &&
			nullSafeEquals(getDescription(), other.getDescription()) &&
			nullSafeEquals(getDependsOn(), other.getDependsOn()) &&
			nullSafeEquals(getType(), other.getType()) &&
			nullSafeEquals(getRadius(), other.getRadius()) &&
			nullSafeEquals(getShowAtTimeStamp(), other.getShowAtTimeStamp()) &&
			nullSafeEquals(getLng(), other.getLng()) &&
			nullSafeEquals(getLat(), other.getLat()) &&
			nullSafeEquals(getIconUrl(), other.getIconUrl()) &&
			nullSafeEquals(getRoles(), other.getRoles()) ; 
	}

	/*
	 * Only relevant for the Android client
	 */
	private Long runId;
	
	public GeneralItem() {
		setType(getClass().getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
		
	public String getScope() {
		if (scope == null) return "user";
		return scope;
	}

	public void setScope(String scope) {
		if (scope == null) this.scope = "user";
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Dependency getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(Dependency dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Long getShowAtTimeStamp() {
		return showAtTimeStamp;
	}

	public void setShowAtTimeStamp(Long showAtTimeStamp) {
		this.showAtTimeStamp = showAtTimeStamp;
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
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public boolean timeStampCheck() {
		//TODO implement
		return true;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public boolean isMessage() {
		return (getLat() == null && getLng() == null);
	}


	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

//	@Override
//	public boolean equals(Object obj) {
//		return this.toString().equals(obj.toString());
//	}
	
	
	
}