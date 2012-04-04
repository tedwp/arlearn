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
