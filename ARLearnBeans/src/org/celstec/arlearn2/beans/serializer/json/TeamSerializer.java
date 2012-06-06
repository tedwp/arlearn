package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.Team;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TeamSerializer  extends RunBeanSerialiser {

	@Override
	public JSONObject toJSON(Object bean) {
		Team runBean = (Team) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getTeamId() != null) returnObject.put("teamId", runBean.getTeamId());
			if (runBean.getName() != null) returnObject.put("name", runBean.getName());
			if (runBean.getUsers() != null) returnObject.put("users", ListSerializer.toJSON(runBean.getUsers()));


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}


}
