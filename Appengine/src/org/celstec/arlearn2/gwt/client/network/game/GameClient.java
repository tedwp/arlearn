package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONObject;

public class GameClient extends GenericClient {

	private static GameClient instance;
	private GameClient() {
	}
	
	public static GameClient getInstance() {
		if (instance == null) instance = new GameClient();
		return instance;
	}

//	public static void createGame(String title, String creator) {
//		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
//		builder.setHeader("Authorization", "GoogleLogin auth="
//				+ Authentication.getInstance().getAuthenticationToken());
//		builder.setHeader("Accept", "application/json");
//		builder.setHeader("Content-Type", "application/json");
//		
//	}

	public void createGame(String title, String creator, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("title", new JSONString(title));
		object.put("creator", new JSONString(creator));
		invokeJsonPOST(null, object, jcb);
	}
	
	public void getGames(final JsonCallback jcb) {
		invokeJsonGET(null, jcb);
	}

	public void deleteGame(long id, final JsonCallback jcb) {
		invokeJsonDELETE("/gameId/"+id, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "myGames";
	}

}
