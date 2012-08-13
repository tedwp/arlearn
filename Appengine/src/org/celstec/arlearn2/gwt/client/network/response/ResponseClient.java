package org.celstec.arlearn2.gwt.client.network.response;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.action.ActionClient;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class ResponseClient extends GenericClient {

	private static ResponseClient instance;
	private ResponseClient() {
	}
	
	public static ResponseClient getInstance() {
		if (instance == null) instance = new ResponseClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "response";
	}
	
	public void createResponse(long id,long runId,String responseValue,String email, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("generalItemId", new JSONNumber(id));
		object.put("runId", new JSONNumber(runId));
		object.put("userEmail", new JSONString(email));
		object.put("responseValue", new JSONString(responseValue));
		invokeJsonPOST(null, object, jcb);
	}
	public void getResponses(long runId, String account, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId+"/account/"+account, jcb);
	}
	
	public void getResponses(long runId, long itemId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId+"/itemId/"+itemId, jcb);
	}
}