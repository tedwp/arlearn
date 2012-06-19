package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateConfigSerializer  extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		LocationUpdateConfig luc = (LocationUpdateConfig) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (luc.getDelay()!= null) returnObject.put("delay", luc.getDelay());
			if (luc.getTimeBetweenUpdates()!= null) returnObject.put("timeBetweenUpdates", luc.getTimeBetweenUpdates());
			if (luc.getRole()!= null) returnObject.put("role", luc.getRole());
			if (luc.getScope()!= null) returnObject.put("scope", luc.getScope());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
