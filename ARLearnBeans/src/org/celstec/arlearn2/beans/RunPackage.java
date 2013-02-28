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
package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunPackage extends Bean{
	private Run run;
	private List<Team> teams = new ArrayList<Team>();
	
	public RunPackage() {
		
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}
	
	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public static class RunPackageDeserializer extends BeanDeserializer{

		@Override
		public RunPackage toBean(JSONObject object) {
			RunPackage bean = new RunPackage();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			RunPackage bean = (RunPackage) genericBean;
			if (object.has("run")) bean.setRun((Run) JsonBeanDeserializer.deserialize(Run.class, object.getJSONObject("run")));
			if (object.has("teams")) bean.setTeams(ListDeserializer.toBean(object.getJSONArray("teams"), Team.class));
		}
		
	}
	
	public static class RunPackageSerializer extends BeanSerializer {

		@Override
		public JSONObject toJSON(Object bean) {
			RunPackage runPackage = (RunPackage) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (runPackage.getRun() != null) returnObject.put("run", JsonBeanSerialiser.serialiseToJson(runPackage.getRun()));
				if (runPackage.getTeams() != null) returnObject.put("teams", ListSerializer.toJSON(runPackage.getTeams()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	}


}
