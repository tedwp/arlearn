package org.celstec.arlearn2.beans.deserializer.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class ListDeserializer {

	
	public static List toBean(JSONArray object, Class c) throws JSONException {
		ArrayList returnList = new ArrayList();
		for (int i = 0; i < object.length();i++) {
			returnList.add( JsonBeanDeserializer.deserialize(c, object.getJSONObject(i)));
			
		}
		return returnList;
	}
	
	public static List toStringList(JSONArray object) throws JSONException {
		ArrayList returnList = new ArrayList();
		for (int i = 0; i < object.length();i++) {
			returnList.add(object.getString(i));
		}
		return returnList;
	}

}
