package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;

public class LocationList extends RunBean{

	public static String locationsType = "org.celstec.arlearn2.beans.run.Location";
	
	private List<Location> locations = new ArrayList<Location>();

	public LocationList() {

	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
	public void addLocation(Location l) {
		this.locations.add(l);
	}
	
	public void addLocations(Location[] loc) {
		for (int i = 0; i < loc.length; i++) {
			addLocation(loc[i]);
		}
		
	}
}
