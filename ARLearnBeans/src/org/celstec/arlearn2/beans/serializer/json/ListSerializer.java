package org.celstec.arlearn2.beans.serializer.json;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;

public class ListSerializer {
	private static final Logger logger = Logger.getLogger(ListSerializer.class.getName());

	public static JSONArray toJSON(Object bean) {
		List list = (List) bean;
		JSONArray array = new JSONArray();
		for (Object b: list) {
			if (b == null) {
				logger.log(Level.SEVERE, "b is null for list "+bean.getClass());
			} else {
				array.put(JsonBeanSerialiser.serialiseToJson(b));	
			}
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
