package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.UserList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserListSerializer extends RunBeanSerialiser{

	@Override
	public JSONObject toJSON(Object bean) {
		UserList ul = (UserList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (ul.getUsers() != null) returnObject.put("users", ListSerializer.toJSON(ul.getUsers()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
