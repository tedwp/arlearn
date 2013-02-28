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
package org.celstec.arlearn2.gwt.client.network.generalItem;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;

public class GeneralItemsClient extends GenericClient {
	private static GeneralItemsClient instance;
	private GeneralItemsClient() {
	}
	
	public static GeneralItemsClient getInstance() {
		if (instance == null) instance = new GeneralItemsClient();
		return instance;
	}
	
	public void getGeneralItemsRun(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
	public void getGeneralItemsGame(long gameId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId, jcb);
	}
	
	public void getGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public void createGeneralItem(JSONObject object, JsonCallback jcb) {
		invokeJsonPOST(null, object, jcb);
	}
	
	public void deleteGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonDELETE("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "generalItems";
	}
}

