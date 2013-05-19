package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public abstract class Bean {

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
	
	public int getInteger(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return (int) jsonRep.get(fieldName).isNumber().doubleValue();
		}
		return 0;
	}
	
	public void setString(String fieldName, String value) {
		jsonRep.put(fieldName, new JSONString(value));
	}
	
	public void setLong(String fieldName, long value) {
		jsonRep.put(fieldName, new JSONNumber(value));
	}
	
	public String getValueAsString(String key) {
		if (jsonRep.containsKey(key)) {
			return jsonRep.get(key).isString().stringValue();
		}
		return null;
	}
	
	public long getValueAsLong(String key) {
		if (jsonRep.containsKey(key)) {
			return (long) jsonRep.get(key).isNumber().doubleValue();
		}
		return 0l;
	}
	
	public JSONObject getJsonRep(){
		return jsonRep;
	}
	
	public void removeAttribute(String idField) {
		if (jsonRep.containsKey(idField)) {
			jsonRep.put(idField, null);
		}
		
	}
	public abstract String getType();
}
