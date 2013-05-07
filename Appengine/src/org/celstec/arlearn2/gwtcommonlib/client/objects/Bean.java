package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class Bean {

	protected JSONObject jsonRep;

	public Bean(JSONObject json) {
		this.jsonRep = json;
	}
	
	public Bean() {
		this.jsonRep = new JSONObject();
		this.jsonRep.put("type", new JSONString(getType()));
	}
	
	public String getString(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isString().stringValue();
		}
		return "";
	}
	
	public void setString(String fieldName, String value) {
		jsonRep.put(fieldName, new JSONString(value));
	}
	
	public JSONObject getJsonRep(){
		return jsonRep;
	}
	
	public String getType() {
		return "";
	}
}
