package org.celstec.arlearn2.android.db.beans;

import java.util.ArrayList;
import java.util.List;


public class LocationList {
	public static String locationsType = "org.celstec.arlearn2.android.db.beans.Location";

	private Long timestamp;
	private List<LocationOld> locations = new ArrayList<LocationOld>();

	public LocationList() {

	}
	
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public List<LocationOld> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationOld> locations) {
		this.locations = locations;
	}

	public void addLocation(LocationOld l) {
		this.locations.add(l);
	}

	public void addLocations(LocationOld[] loc) {
		for (int i = 0; i < loc.length; i++) {
			addLocation(loc[i]);
		}
		
	}
}
