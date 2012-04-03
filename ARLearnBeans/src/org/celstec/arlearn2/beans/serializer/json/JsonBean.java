package org.celstec.arlearn2.beans.serializer.json;

import org.codehaus.jettison.json.JSONObject;

public interface JsonBean {

	public JSONObject toJSON(Object bean);
}
