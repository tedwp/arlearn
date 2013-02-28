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
package org.celstec.arlearn2.delegators.notification;

import java.util.HashMap;

public class LocationNotification extends Notification {
	public static final String LAT = "LAT";
	public static final String LNG = "LNG";

	public LocationNotification(int scope, int runId, String authToken) {
		super(scope, runId, authToken);
	}
	
	public void locationChanged(double lat, double lng) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("user", getUser());
		hm.put("lat", ""+lat);
		hm.put("lng", ""+lng);
		notify("LocationUpdate", hm);
	}
}
