package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONBoolean;
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
	
	public Double getDouble(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isNumber().doubleValue();
		}
		return null;
	}
	
	public boolean getBoolean(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isBoolean().booleanValue();
		}
		return false;
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
	
	public void setBoolean(String fieldName, boolean value) {
		jsonRep.put(fieldName, JSONBoolean.getInstance(value));
	}
	
	public void setLong(String fieldName, long value) {
		jsonRep.put(fieldName, new JSONNumber(value));
	}
	
	public void setDouble(String fieldName, double value) {
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
