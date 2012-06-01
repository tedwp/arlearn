package org.celstec.arlearn2.gwt.client.network.team;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class TeamClient extends GenericClient {
	
	private static TeamClient instance;
	private TeamClient() {
	}
	
	public static TeamClient getInstance() {
		if (instance == null) instance = new TeamClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "team";
	}
	
	public void createTeam(Long runId, String name, JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("name", new JSONString(name));
		object.put("runId", new JSONNumber(runId));
		invokeJsonPOST(null, object, jcb);		
	}

	public void deleteTeam(String teamId, JsonCallback jsonCallback) {
		invokeJsonDELETE("/teamId/"+teamId, jsonCallback);
		
	}
}
