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
package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.network.ConnectionFactory;

public class LocationClient extends GenericClient{

	private static LocationClient instance;

	private LocationClient() {
		super("/location");
	}
	
	public static LocationClient getLocationClient() {
		if (instance == null) {
			instance = new LocationClient();
		}
		return instance;
	}
	
	public LocationUpdate postLocation(String token, long runId, LocationUpdate locUpdate) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix()+"/runId/"+runId, token, "application/json", toJson(locUpdate), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
			return (LocationUpdate) jsonDeserialise(entry, LocationUpdate.class);
		} catch (Exception e) {
			e.printStackTrace();
			locUpdate = new LocationUpdate();
			locUpdate.setError("exception "+e.getMessage());
			return locUpdate;
		}
	}

    public void submitLocation(String token, double lat, double lng, long time) {
        String postString = "lat="+lat+"&lng="+lng+"&time="+time;
        HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", postString, "application/json");
        String entry;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
