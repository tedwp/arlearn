package org.celstec.arlearn2.beans.serializer.json;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONStringer;

public class ListSerializer {

	public static JSONArray toJSON(Object bean) {
		List list = (List) bean;
		JSONArray array = new JSONArray();
		for (Object b: list) {
			array.put(JsonBeanSerialiser.serialiseToJson(b));
		}
		return array;
	}
	
	public static JSONArray toStringList(List<String> list) {
		JSONArray array = new JSONArray();
		for (String b: list) {
				array.put(b);
		}
		return array;
	}

}
