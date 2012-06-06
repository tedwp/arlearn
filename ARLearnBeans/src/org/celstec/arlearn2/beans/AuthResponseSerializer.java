package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AuthResponseSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		AuthResponse authBean = (AuthResponse) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (authBean.getAuth() != null) returnObject.put("auth", authBean.getAuth());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
