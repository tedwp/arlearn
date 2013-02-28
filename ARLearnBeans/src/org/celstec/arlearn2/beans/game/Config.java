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
