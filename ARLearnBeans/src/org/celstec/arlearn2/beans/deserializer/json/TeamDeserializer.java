package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TeamDeserializer extends RunBeanDeserializer{

	@Override
	public Team toBean(JSONObject object) {
		Team bean = new Team();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Team bean = (Team) genericBean;
		if (object.has("name")) bean.setName(object.getString("name"));
		if (object.has("teamId")) bean.setTeamId(object.getString("teamId"));
		if (object.has("users")) bean.setUsers(ListDeserializer.toBean(object.getJSONArray("users"), User.class));
//		if (object.has("teams")) bean.setTeams(ListDeserializer.toBean(object.getJSONArray("teams"), Team.class));


	}


}
