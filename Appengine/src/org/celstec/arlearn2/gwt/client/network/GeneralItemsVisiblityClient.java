package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class GeneralItemsVisiblityClient extends GenericClient {

	private static GeneralItemsVisiblityClient instance;
	private GeneralItemsVisiblityClient() {
	}
	
	public static GeneralItemsVisiblityClient getInstance() {
		if (instance == null) instance = new GeneralItemsVisiblityClient();
		return instance;
	}
	
	public void makeItemVisible(long id,long runId, long atTime,String email, int status, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("generalItemId", new JSONNumber(id));
		object.put("email", new JSONString(email));
		object.put("runId", new JSONNumber(runId));
		object.put("timeStamp", new JSONNumber(atTime));
		object.put("status", new JSONNumber(status));
		invokeJsonPOST("/runId/"+runId+"/account/"+email, object, jcb);
	}
	

	public String getUrl() {
		return super.getUrl() + "generalItemsVisibility";
	}
}
