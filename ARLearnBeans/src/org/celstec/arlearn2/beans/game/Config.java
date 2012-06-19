
package org.celstec.arlearn2.beans.game;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class Config extends Bean {

	
	private Boolean scoring;
	private Boolean mapAvailable;
	public static String manualItemsType = "org.celstec.arlearn2.beans.generalItem.GeneralItem";
	private List<GeneralItem> manualItems = new ArrayList<GeneralItem>();
	private List<LocationUpdateConfig> locationUpdates = new ArrayList<LocationUpdateConfig>();
	private List<String> roles;

	@Override
	public boolean equals(Object obj) {
		Config other = (Config ) obj;
		
//		List<GeneralItem> list1 = getManualItems();list1.equals(o)
//		List<GeneralItem> list2 = other.getManualItems();
//		if (!(list1.size() == list2.size())) return false;
//		for (int i = 0 ; i< list1.size();i++) {
//			if (!(list1.get(i).equals(list2.get(i)))) return false;
//		}
		
		return super.equals(obj) && 
			nullSafeEquals(getManualItems(), other.getManualItems()) && 	
			nullSafeEquals(getLocationUpdates(), other.getLocationUpdates()) && 	
			nullSafeEquals(getScoring(), other.getScoring()) && 
			nullSafeEquals(getRoles(), other.getRoles()) && 

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
