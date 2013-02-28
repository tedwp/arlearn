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
package org.celstec.arlearn2.gwt.client.network.user;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class UserClient  extends GenericClient {
	
	private static UserClient instance;
	private UserClient() {
	}
	
	public static UserClient getInstance() {
		if (instance == null) instance = new UserClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "users";
	}
	
	public void createUser(Long runId, String teamId, String email, String name, String[] roles, JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("teamId", new JSONString(teamId));
		object.put("email", new JSONString(email));
		object.put("name", new JSONString(name));
		object.put("runId", new JSONNumber(runId));
		if (roles != null && roles.length != 0) {
			JSONArray ar = new JSONArray();
			object.put("roles", ar);
			for (int i = 0; i <roles.length; i++) {
				ar.set(i, new JSONString(roles[i]));
			}
		}
		invokeJsonPOST(null, object, jcb);		
	}
	
	public void deleteUser(Long runId, String email, JsonCallback jcb) {
		invokeJsonDELETE("/runId/"+runId+"/email/"+email, jcb);
	}
	
	

}
