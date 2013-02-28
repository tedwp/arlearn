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
package org.celstec.arlearn2.gwt.client.network.run;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class RunClient extends GenericClient {

	private static RunClient instance;
	private RunClient() {
	}
	
	public static RunClient getInstance() {
		if (instance == null) instance = new RunClient();
		return instance;
	}
	
	public void createRun(Object gameId, String title, JsonCallback jcb) {
		if (!(gameId instanceof Integer)) return;
		Integer id = (Integer) gameId;
		JSONObject object = new JSONObject();
		object.put("title", new JSONString(title));
		object.put("gameId", new JSONNumber(id));
		invokeJsonPOST(null, object, jcb);		
	}
	
	public void updateRun(long runId, JSONObject run, JsonCallback jcb) {
		invokeJsonPUT("/runId/"+runId, run.toString(), jcb);		

	}
	
	public void runsParticapte(final JsonCallback jcb) {
		invokeJsonGET("/participate", jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "myRuns";
	}

}
