package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class ActionClient  extends GenericClient {

	private static ActionClient instance;
	private ActionClient() {
	}
	
	public static ActionClient getInstance() {
		if (instance == null) instance = new ActionClient();
		return instance;
	}
	

	public void createAction(long id,long runId,String action,String email, String type, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("generalItemId", new JSONNumber(id));
		object.put("runId", new JSONNumber(runId));
		object.put("generalItemType", new JSONString(type));
		object.put("userEmail", new JSONString(email));
		object.put("action", new JSONString(action));
		invokeJsonPOST(null, object, jcb);
	}
	
	public void getActions(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "actions";
	}

	public void notify(long runId, String email, String itemId, boolean autoLaunch, final JsonCallback jsonCallback) {
		invokeJsonPOST("/notify/"+email+"/"+runId+"/"+itemId+"/"+autoLaunch, "", jsonCallback);
		
	}
}
