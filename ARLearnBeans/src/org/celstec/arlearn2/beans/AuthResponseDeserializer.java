package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AuthResponseDeserializer extends BeanDeserializer{
	
	@Override
	public AuthResponse toBean(JSONObject object) {
		AuthResponse returnObject = new AuthResponse();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		AuthResponse oq = (AuthResponse) genericBean;
		if (object.has("auth")) oq.setAuth(object.getString("auth"));
	}

}
