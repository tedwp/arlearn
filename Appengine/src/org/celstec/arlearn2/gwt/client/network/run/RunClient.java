package org.celstec.arlearn2.gwt.client.network.run;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class RunClient extends GenericClient {

	private static RunClient instance;
	private RunClient() {
	}
	
	public static RunClient getInstance() {
		if (instance == null) instance = new RunClient();
		return instance;
	}
	
	public void createRun(Object gameId, String title, JsonCallback jcb) {
		if (!(gameId instanceof Integer)) return;
		Integer id = (Integer) gameId;
		JSONObject object = new JSONObject();
		object.put("title", new JSONString(title));
		object.put("gameId", new JSONNumber(id));
		invokeJsonPOST(null, object, jcb);		
	}
	
	public String getUrl() {
		return super.getUrl() + "myRuns";
	}

}
