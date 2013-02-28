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
package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

@Deprecated
public class ResponseClient extends GenericClient {

//	private static ResponseClient instance;
//	private ResponseClient() {
//	}
//	
//	public static ResponseClient getInstance() {
//		if (instance == null) instance = new ResponseClient();
//		return instance;
//	}
//	
//	public void getResponses(long runId, String account, final JsonCallback jcb) {
//		invokeJsonGET("/runId/"+runId+"/account/"+account, jcb);
//	}
//	
//	public void getResponses(long runId, long itemId, final JsonCallback jcb) {
//		invokeJsonGET("/runId/"+runId+"/itemId/"+itemId, jcb);
//	}
//	
//	public void createResponse(long id,long runId,String responseValue,String email, final JsonCallback jcb) {
//		JSONObject object = new JSONObject();
//		object.put("generalItemId", new JSONNumber(id));
//		object.put("runId", new JSONNumber(runId));
//		object.put("userEmail", new JSONString(email));
//		object.put("responseValue", new JSONString(responseValue));
//		invokeJsonPOST(null, object, jcb);
//	}
//	
//	public String getUrl() {
//		return super.getUrl() + "response";
//	}
	
}
