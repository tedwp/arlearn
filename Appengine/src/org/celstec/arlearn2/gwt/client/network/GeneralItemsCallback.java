package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class GeneralItemsCallback extends JsonCallback {

	private JSONArray jsonValue;

	@Override
	public void onJsonReceived(JSONValue jsonValue) {
		if (jsonValue.isObject().get("generalItems") != null
				&& jsonValue.isObject().get("generalItems").isArray() != null) {
			this.jsonValue = jsonValue.isObject().get("generalItems").isArray();
		}
		onGeneralItemsReady();
		
	}

	@Override
	public abstract void onError() ;

	public abstract void onGeneralItemsReady();
	
	public int giSize() {
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public String getItemName(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("name").isString().stringValue();
	}
	
	public String getItemType(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		String typeValue =  game.get("type").isString().stringValue();
		typeValue = typeValue.substring(typeValue.lastIndexOf(".")+1);
		return typeValue;
	}
	
	public long getItemId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("id").isNumber().doubleValue();
	
	}
	
	public int getSortKey(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		if (!game.containsKey("sortKey")) return 0;
		return (int) game.get("sortKey").isNumber().doubleValue();
	}
	public long getGameId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("gameId").isNumber().doubleValue();
	}
}
