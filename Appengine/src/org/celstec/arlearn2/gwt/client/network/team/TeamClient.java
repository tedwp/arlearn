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
package org.celstec.arlearn2.gwt.client.network.team;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class TeamClient extends GenericClient {
	
	private static TeamClient instance;
	private TeamClient() {
	}
	
	public static TeamClient getInstance() {
		if (instance == null) instance = new TeamClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "team";
	}
	
	public void createTeam(Long runId, String name, JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("name", new JSONString(name));
		object.put("runId", new JSONNumber(runId));
		invokeJsonPOST(null, object, jcb);		
	}

	public void deleteTeam(String teamId, JsonCallback jsonCallback) {
		invokeJsonDELETE("/teamId/"+teamId, jsonCallback);
		
	}
}
