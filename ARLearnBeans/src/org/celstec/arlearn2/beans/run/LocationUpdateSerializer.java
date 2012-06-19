package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		LocationUpdate lu = (LocationUpdate) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (lu.getLat() != null) returnObject.put("lat", lu.getLat());
			if (lu.getLng() != null) returnObject.put("lng", lu.getLng());
			if (lu.getAccount() != null) returnObject.put("account", lu.getAccount());
			if (lu.getRecepientType() != null) returnObject.put("recepientType", lu.getRecepientType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
