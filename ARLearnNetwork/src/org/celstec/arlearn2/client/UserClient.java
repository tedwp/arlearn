package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.User;

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
		return (LocationList) executePost(getUrlPrefix()+ "/locations/runId/"+runId, token, locations, LocationList.class);
	}
	
	public User getUser(String token, Long runId, String userEmail) {
		return (User)  executeGet(getUrlPrefix()+"/loggedInUser/"+runId+"/email/"+userEmail, token, User.class);
	}
	
	public GeneralItemList getRunGeneralItems(String token, Long runId) {
		return (GeneralItemList)  executeGet(getUrlPrefix()+"/runId/"+runId, token, GeneralItemList.class);
	}
	
	public User createUser(String token, User user) {
		return (User) executePost(getUrlPrefix(), token, user, User.class);
	}
}
