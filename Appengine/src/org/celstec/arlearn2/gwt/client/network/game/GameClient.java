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
package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONObject;

public class GameClient extends GenericClient {

	private static GameClient instance;
	private GameClient() {
	}
	
	public static GameClient getInstance() {
		if (instance == null) instance = new GameClient();
		return instance;
	}

//	public static void createGame(String title, String creator) {
//		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
//		builder.setHeader("Authorization", "GoogleLogin auth="
//				+ Authentication.getInstance().getAuthenticationToken());
//		builder.setHeader("Accept", "application/json");
//		builder.setHeader("Content-Type", "application/json");
//		
//	}

	public void createGame(String title, String creator, boolean withMap, final JsonCallback jcb) {
		JSONObject object = new JSONObject();
		object.put("title", new JSONString(title));
		if (creator != null) object.put("creator", new JSONString(creator));
		JSONObject config = new JSONObject();
		object.put("config", config);
		config.put("mapAvailable", JSONBoolean.getInstance(withMap));
		invokeJsonPOST(null, object, jcb);
	}
	
	public void getGames(final JsonCallback jcb) {
		invokeJsonGET(null, jcb);
	}
	
	public void getGame(long id,final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+id, jcb);
	}
	
	
	public void getGameConfig(long id, final JsonCallback jcb) {
		invokeJsonGET("/config/gameId/"+id, jcb);
	}
	
	public void installManualTrigger(long gameId, String item, final JsonCallback jcb) {
		invokeJsonPOST("/config/manualtrigger/gameId/"+gameId, item, jcb);
	}
	
	public void removeManualTrigger(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonGET("/config/removeManualTrigger/gameId/"+gameId+"/itemId/"+itemId, jcb);
	}
		
	public void deleteGame(long id, final JsonCallback jcb) {
		invokeJsonDELETE("/gameId/"+id, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "myGames";
	}

	public void createRole(long gameId, String roleValue, JsonCallback jsonCallback) {
		invokeJsonPOST("/config/gameId/"+gameId+"/role", roleValue, jsonCallback);
	}
	
	public void setMapType(long gameId, int mapType, JsonCallback jsonCallback) {
		invokeJsonPOST("/config/gameId/"+gameId+"/mapType", ""+mapType, jsonCallback);
	}
	
	public void setWithMap(long gameId, Boolean withMap, JsonCallback jsonCallback) {
		invokeJsonPOST("/config/gameId/"+gameId+"/withMap", ""+withMap, jsonCallback);
	}
	
	public void setSharing(long gameId, Integer sharingType, JsonCallback jsonCallback) {
		invokeJsonPOST("/config/gameId/"+gameId+"/sharing/"+sharingType, "", jsonCallback);
	}

	public void addMapRegion(long gameId, JSONArray array, JsonCallback jsonCallback) {
		invokeJsonPOST("/config/gameId/"+gameId+"/mapRegion", ""+array.toString(), jsonCallback);
		
	}

}
