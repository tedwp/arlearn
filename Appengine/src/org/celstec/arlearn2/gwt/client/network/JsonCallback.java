package org.celstec.arlearn2.gwt.client.network;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGrid;

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
			if (!game.containsKey(attributeName)) return null;
			if (game.get(attributeName).isArray()!=null) {
				JSONArray ar = game.get(attributeName).isArray();
				String returnString = "";
				if (ar.size()>=1) returnString = ar.get(0).isString().stringValue();
				for (int j = 1; j< ar.size(); j++) {
					returnString += ", "+ar.get(j).isString().stringValue();
				}
				return returnString;
			}
			return game.get(attributeName).isString().stringValue();
	}
	
	public int getAttributeInteger(int i, String attributeName) {
			if (jsonValue == null) return -1;
			JSONObject game = jsonValue.get(i).isObject();
			if (game == null) return -1;
//			System.out.println("attribute: "+attributeName +" game");
			if (!game.containsKey(attributeName) ) return 0;
			return (int) game.get(attributeName).isNumber().doubleValue();
	}
	
	public JSONObject getGameConfig(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		if (game.get("config") == null) return null;
		return game.get("config").isObject();
	}

//	protected ListGrid lg;
//	
//	public void setFetchListGrid(ListGrid lg) {
//		this.lg = lg;
//		
//	}
	
}
