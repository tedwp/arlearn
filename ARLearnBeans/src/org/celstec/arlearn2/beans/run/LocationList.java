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
