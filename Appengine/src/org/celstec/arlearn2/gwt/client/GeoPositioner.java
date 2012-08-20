package org.celstec.arlearn2.gwt.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;

public class GeoPositioner {

	private static GeoPositioner instance;
	private static Coordinates coordinates;
	
	private GeoPositioner () {
		if (Geolocation.isSupported()) {
			Geolocation.getIfSupported().getCurrentPosition(new Callback<Position, PositionError>() {
				
				@Override
				public void onSuccess(Position result) {
					coordinates = result.getCoordinates();
				}
				
				@Override
				public void onFailure(PositionError reason) {
					
				}
			});

		}
	}
	
	public static GeoPositioner getInstance() {
		if (instance == null) {
			instance = new GeoPositioner();
		}
		return instance;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
}
