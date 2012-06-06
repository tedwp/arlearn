package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.TeamList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TeamListSerializer  extends RunBeanSerialiser{

	@Override
	public JSONObject toJSON(Object bean) {
		TeamList tl = (TeamList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (tl.getTeams() != null) returnObject.put("teams", ListSerializer.toJSON(tl.getTeams()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
