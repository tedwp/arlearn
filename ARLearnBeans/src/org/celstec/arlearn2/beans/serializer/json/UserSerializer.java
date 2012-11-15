package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.User;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserSerializer extends RunBeanSerialiser {

	@Override
	public JSONObject toJSON(Object bean) {
		User runBean = (User) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getTeamId() != null) returnObject.put("teamId", runBean.getTeamId());
			if (runBean.getName() != null) returnObject.put("name", runBean.getName());
			if (runBean.getEmail() != null) returnObject.put("email", runBean.getEmail());
			if (runBean.getFullEmail() != null) returnObject.put("fullEmail", runBean.getFullEmail());
			if (runBean.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(runBean.getRoles()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
