package org.celstec.arlearn2.gwt.client.network;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class JsonCallback {

	private String type;
	private JSONArray jsonValue;
	protected ReadyCallback rc;
	
	public JsonCallback() {
		
	}
	
	public void setReadyCallback(ReadyCallback rc) {
		this.rc = rc;
	}
	
	public JsonCallback(String type) {
		this.type = type;
	}
	
	public void onJsonReceived(JSONValue jsonValue) {
			if (type != null && jsonValue.isObject().get(type) != null && jsonValue.isObject().get(type).isArray() != null) {
				this.jsonValue = jsonValue.isObject().get(type).isArray();
			}
			onReceived();
	}
	
	public void onError(){
	
	}
	//todo make abstract
	public void onReceived(){};
	
	public int size(){
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public String getAttributeString(int i, String attributeName) {
			if (jsonValue == null) return null;
			JSONObject game = jsonValue.get(i).isObject();
			if (game == null) return null;
			return game.get(attributeName).isString().stringValue();
	}
	
	public int getAttributeInteger(int i, String attributeName) {
			if (jsonValue == null) return -1;
			JSONObject game = jsonValue.get(i).isObject();
			if (game == null) return -1;
//			System.out.println("attribute: "+attributeName +" game");
			return (int) game.get(attributeName).isNumber().doubleValue();
	}
	
	public JSONObject getGameConfig(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		if (game.get("config") == null) return null;
		return game.get("config").isObject();
	}
}
