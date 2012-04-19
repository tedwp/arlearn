package org.celstec.arlearn2.gwt.client.network.user;

import org.celstec.arlearn2.gwt.client.network.GenericClient;

public class UserClient  extends GenericClient {
	
	private static UserClient instance;
	private UserClient() {
	}
	
	public static UserClient getInstance() {
		if (instance == null) instance = new UserClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "users";
	}

}
