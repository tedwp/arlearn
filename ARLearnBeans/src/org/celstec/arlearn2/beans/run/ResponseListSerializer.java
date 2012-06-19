package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseListSerializer  extends RunBeanSerialiser{

	@Override
	public JSONObject toJSON(Object bean) {
		ResponseList al = (ResponseList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (al.getResponses() != null) returnObject.put("responses", ListSerializer.toJSON(al.getResponses()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
