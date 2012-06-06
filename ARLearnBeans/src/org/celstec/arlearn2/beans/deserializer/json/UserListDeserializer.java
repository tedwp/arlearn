package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserListDeserializer extends BeanDeserializer{

	@Override
	public UserList toBean(JSONObject object) {
		UserList ul = new UserList();
		try {
			initBean(object, ul);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ul;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		UserList userlist = (UserList) genericBean;
		if (object.has("users")) userlist.setUsers(ListDeserializer.toBean(object.getJSONArray("users"), User.class));
	}

}
