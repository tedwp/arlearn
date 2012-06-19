package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateConfigDeserializer extends BeanDeserializer{
	
	@Override
	public LocationUpdateConfig toBean(JSONObject object) {
		LocationUpdateConfig returnObject = new LocationUpdateConfig();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		LocationUpdateConfig luc = (LocationUpdateConfig) genericBean;
		if (object.has("delay")) luc.setDelay(object.getLong("delay"));
		if (object.has("timeBetweenUpdates")) luc.setTimeBetweenUpdates(object.getLong("timeBetweenUpdates"));
		if (object.has("role")) luc.setRole(object.getString("role"));
		if (object.has("scope")) luc.setScope(object.getString("scope"));
		
	}

}
