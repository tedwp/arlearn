package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class ActionsCallback extends JsonCallback {

	private JSONArray jsonValue;

	@Override
	public void onJsonReceived(JSONValue jsonValue) {
		if (jsonValue.isObject().get("actions") != null
				&& jsonValue.isObject().get("actions").isArray() != null) {
			this.jsonValue = jsonValue.isObject().get("actions").isArray();
		}
		onActionsReady();
	}

	@Override
	public abstract void onError();

	public abstract void onActionsReady();

	public int actionsSize() {
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public long getGeneralItemId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("generalItemId").isNumber().doubleValue();
	}
	
	public String getGeneralItemType(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("generalItemType").isString().stringValue();
	}
	
	public String getUserEmail(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("userEmail").isString().stringValue();
	}
	
	public String getAction(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("action").isString().stringValue();
	}
	
	public long getRunId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("runId").isNumber().doubleValue();
	}

}
