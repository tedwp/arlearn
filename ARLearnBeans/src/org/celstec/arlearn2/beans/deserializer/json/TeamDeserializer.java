/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
