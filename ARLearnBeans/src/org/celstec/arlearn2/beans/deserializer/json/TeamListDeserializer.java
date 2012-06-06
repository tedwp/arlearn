package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TeamListDeserializer extends BeanDeserializer{

	@Override
	public TeamList toBean(JSONObject object) {
		TeamList tl = new TeamList();
		try {
			initBean(object, tl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tl;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		TeamList teamlist = (TeamList) genericBean;
		if (object.has("teams")) teamlist.setTeams(ListDeserializer.toBean(object.getJSONArray("teams"), Team.class));
	}

}
