package org.celstec.arlearn2.gwtcommonlib.client.network;

public class UserClient extends GenericClient {

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
	
	public void getUsers(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
}
