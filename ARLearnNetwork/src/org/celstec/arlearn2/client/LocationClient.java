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
}
