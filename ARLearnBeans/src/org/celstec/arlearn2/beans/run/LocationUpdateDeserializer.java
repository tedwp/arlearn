package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateDeserializer extends BeanDeserializer{
	
	@Override
	public LocationUpdate toBean(JSONObject object) {
		LocationUpdate returnObject = new LocationUpdate();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		LocationUpdate oq = (LocationUpdate) genericBean;
		if (object.has("lat")) oq.setLat(object.getDouble("lat"));
		if (object.has("lng")) oq.setLng(object.getDouble("lng"));
		if (object.has("account")) oq.setAccount(object.getString("account"));
		if (object.has("recepientType")) oq.setRecepientType(object.getInt("recepientType"));
	}

}
