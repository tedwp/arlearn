package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.User;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserDeserializer extends RunBeanDeserializer{

	@Override
	public User toBean(JSONObject object) {
		User bean = new User();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		User bean = (User) genericBean;
		if (object.has("name")) bean.setName(object.getString("name"));
		if (object.has("teamId")) bean.setTeamId(object.getString("teamId"));
		if (object.has("email")) bean.setEmail(object.getString("email"));
		if (object.has("fullEmail")) bean.setFullEmail(object.getString("fullEmail"));
		if (object.has("roles")) bean.setRoles(ListDeserializer.toStringList(object.getJSONArray("roles")));

	}

}
