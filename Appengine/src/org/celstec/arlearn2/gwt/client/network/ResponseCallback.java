package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class ResponseCallback extends JsonCallback {

	private JSONArray jsonValue;

	@Override
	public void onJsonReceived(JSONValue jsonValue) {
		if (jsonValue.isObject().get("responses") != null
				&& jsonValue.isObject().get("responses").isArray() != null) {
			this.jsonValue = jsonValue.isObject().get("responses").isArray();
		}
		onResponsesReady();
	}

	@Override
	public abstract void onError();

	public abstract void onResponsesReady();

	public int responsesSize() {
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public long getGeneralItemId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		if (!game.containsKey("generalItemId")) return -1;
		return (long) game.get("generalItemId").isNumber().doubleValue();
	}
	
	public String getUserEmail(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("userEmail").isString().stringValue();
	}
	
	public String getResponseValue(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("responseValue").isString().stringValue();
	}
	
	public long getRunId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("runId").isNumber().doubleValue();
	}
	
}
