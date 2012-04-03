package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.network.ConnectionFactory;

public class UserClient extends GenericClient{
	

	private static UserClient instance;

	private UserClient() {
		super("/users");
	}

	public static UserClient getUserClient() {
		if (instance == null) {
			instance = new UserClient();
		}
		return instance;
	}

	public LocationList publishLocations(String token, LocationList locations, Long runId) {
		HttpResponse httpResp = ConnectionFactory.getConnection().executePOST(getUrlPrefix()+ "/locations/runId/"+runId, token, "application/json", toJson(locations), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(httpResp.getEntity());
			return (LocationList) jsonDeserialise(entry, LocationList.class);
		} catch (Exception e) {
			e.printStackTrace();
			LocationList locErrror = new LocationList();
			locErrror.setError("exception "+e.getMessage());
			return locErrror;
		}
	}
	
	public User createUser(String token, User user) {
		return (User) executePost(getUrlPrefix(), token, user, User.class);
	}
}
