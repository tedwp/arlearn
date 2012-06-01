package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BeanDeserializer  extends CustomDeserializer {

	@Override
	public Bean toBean(JSONObject object) {
		Bean bean = new Bean();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean bean) throws JSONException {
		if (object.has("type")) bean.setType(object.getString("type"));
		if (object.has("error")) bean.setError(object.getString("error"));
		if (object.has("errorCode")) bean.setErrorCode(object.getInt("errorCode"));
		if (object.has("timestamp")) bean.setTimestamp(object.getLong("timestamp"));

		
	}
}
