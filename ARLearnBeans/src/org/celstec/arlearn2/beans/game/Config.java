
package org.celstec.arlearn2.beans.game;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class Config extends Bean {

	public static final int MAP_TYPE_GOOGLE_MAPS = 0;
	public static final int MAP_TYPE_OSM = 1;
	
	private Boolean scoring;
	private Boolean mapAvailable;
	private Integer mapType;
	
	private List<MapRegion> mapRegions;
	
	public static String manualItemsType = "org.celstec.arlearn2.beans.generalItem.GeneralItem";
	private List<GeneralItem> manualItems = new ArrayList<GeneralItem>();
	private List<LocationUpdateConfig> locationUpdates = new ArrayList<LocationUpdateConfig>();
	private List<String> roles;

	@Override
	public boolean equals(Object obj) {
		Config other = (Config ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getManualItems(), other.getManualItems()) && 	
			nullSafeEquals(getLocationUpdates(), other.getLocationUpdates()) && 	
			nullSafeEquals(getScoring(), other.getScoring()) && 
			nullSafeEquals(getRoles(), other.getRoles()) && 
			nullSafeEquals(getMapType(), other.getMapType()) && 
			nullSafeEquals(getMapRegions(), other.getMapRegions()) && 
			nullSafeEquals(getMapAvailable(), other.getMapAvailable()); 

	}
	
	public Boolean getScoring() {
		return scoring;
	}

	public void setScoring(Boolean scoring) {
		this.scoring = scoring;
	}

	public Boolean getMapAvailable() {
		return mapAvailable;
	}

	public void setMapAvailable(Boolean mapAvailable) {
		this.mapAvailable = mapAvailable;
	}

	public Integer getMapType() {
		return mapType;
	}

	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}

	public List<MapRegion> getMapRegions() {
		return mapRegions;
	}

	public void setMapRegions(List<MapRegion> mapRegions) {
		this.mapRegions = mapRegions;
	}

	public List<GeneralItem> getManualItems() {
		return manualItems;
	}

	public void setManualItems(List<GeneralItem> manualItems) {
		this.manualItems = manualItems;
	}
	
	public List<LocationUpdateConfig> getLocationUpdates() {
		return locationUpdates;
	}

	public void setLocationUpdates(List<LocationUpdateConfig> locationUpdates) {
		this.locationUpdates = locationUpdates;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
