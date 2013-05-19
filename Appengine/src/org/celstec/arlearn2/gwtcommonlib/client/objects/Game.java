package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class Game extends Bean{

	
	public Game(JSONObject json) {
		this.jsonRep = json;
	}
	
	public Game() {
		super();
		JSONObject config = new JSONObject();
		config.put("type", new JSONString("org.celstec.arlearn2.beans.game.Config"));
		config.put("mapAvailable", JSONBoolean.getInstance(false));
		this.jsonRep.put("config", config);
	}
	
	public String getType() {
		return "org.celstec.arlearn2.beans.game.Game";
	}
	
	public JSONObject getJSON() {
		return jsonRep;
	}
	
	public String[] getRoles() {
		String [] roles = null;
		if (jsonRep.containsKey("config")) {
			if (jsonRep.get("config").isObject().containsKey("roles")){
				JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
				roles = new String[array.size()];
				for (int i = 0; i< array.size();i++) {
					roles[i] = array.get(i).isString().stringValue();
				}
			}
		}
		return roles;
	}
	
	public void deleteRole(String role) {
		if (jsonRep.containsKey("config")) {
			if (jsonRep.get("config").isObject().containsKey("roles")){
				JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
				JSONArray newArray = new JSONArray();
				boolean elementFound = false;
				for (int i = 0; i< array.size();i++) {
					if (role.equals(array.get(i).isString().stringValue())) {
						elementFound = true;
					} else 
					if (elementFound) {
						newArray.set(i-1, array.get(i));
					} else {
						newArray.set(i, array.get(i));
					}
				}
				jsonRep.get("config").isObject().put("roles", newArray);
			}
		}
		
	}
	
	public void addRole(String value) {
		if (!jsonRep.containsKey("config")) jsonRep.put("config", new JSONObject());
		if (!jsonRep.get("config").isObject().containsKey("roles")) jsonRep.get("config").isObject().put("roles", new JSONArray());
		JSONArray array = jsonRep.get("config").isObject().get("roles").isArray();
		array.set(array.size(), new JSONString(value));
	}
	
	public long getGameId() {
		return (long) jsonRep.get("gameId").isNumber().doubleValue();
	}
	
	public int getSharing() {
		int sharing = 1;
		if (jsonRep.containsKey("sharing")) {
			sharing = (int) jsonRep.get("sharing").isNumber().doubleValue();
		}
		return sharing;
	}

	public void setTitle(String title) {
		jsonRep.put(GameModel.GAME_TITLE_FIELD, new JSONString(title));
	}
	
	public String getDescription() {
		if (jsonRep.containsKey("description")) {
			return jsonRep.get("description").isString().stringValue();
		}
		return "";
	}
	
	public void setDescription(String description) {
		jsonRep.put("description", new JSONString(description));

	}
	
	public void writeToCloud(JsonCallback jsonCallback) {
		GameClient.getInstance().createGame(this, jsonCallback);
	}

	

}

//role: {"type":"org.celstec.arlearn2.beans.game.Game", "gameId":3, 
//"lastModificationDate":1366111358755, "title":"ectel", "sharing":1, 
//"config":{"type":"org.celstec.arlearn2.beans.game.Config", "mapAvailable":false,
//	"manualItems":[], "locationUpdates":[], 
//	"roles":["gamemaster","other role"]}, "accessRights":1}
