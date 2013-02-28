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
