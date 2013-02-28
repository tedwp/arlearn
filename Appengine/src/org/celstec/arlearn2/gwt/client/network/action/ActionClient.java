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
package org.celstec.arlearn2.gwt.client.network.action;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class ActionClient extends GenericClient {

	private static ActionClient instance;
	private ActionClient() {
	}
	
	public static ActionClient getInstance() {
		if (instance == null) instance = new ActionClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "actions";
	}
	
	public void notify(long runId, String email, String itemId, final JsonCallback jsonCallback) {
		invokeJsonPOST("/notify/"+email+"/"+runId+"/"+itemId, "", jsonCallback);
		
	}
	
	public void createAction(long id,long runId,String action,String email, String type, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("generalItemId", new JSONNumber(id));
		object.put("runId", new JSONNumber(runId));
		object.put("generalItemType", new JSONString(type));
		object.put("userEmail", new JSONString(email));
		object.put("action", new JSONString(action));
		invokeJsonPOST(null, object, jcb);
	}
}
