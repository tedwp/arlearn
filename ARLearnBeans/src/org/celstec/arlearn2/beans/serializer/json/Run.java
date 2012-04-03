package org.celstec.arlearn2.beans.serializer.json;

import org.codehaus.jettison.json.JSONObject;

public class Run implements JsonBean{

	@Override
	public JSONObject toJSON(Object bean) {
		return new JSONObject();
	}
	
}
