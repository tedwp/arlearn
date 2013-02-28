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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class GeneralItemsCallback extends JsonCallback {

	private JSONArray jsonValue;

	@Override
	public void onJsonReceived(JSONValue jsonValue) {
		if (jsonValue.isObject().get("generalItems") != null
				&& jsonValue.isObject().get("generalItems").isArray() != null) {
			this.jsonValue = jsonValue.isObject().get("generalItems").isArray();
		}
		onGeneralItemsReady();
		
	}

	@Override
	public abstract void onError() ;

	public abstract void onGeneralItemsReady();
	
	public int giSize() {
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public String getItemName(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("name").isString().stringValue();
	}
	
	public boolean getDeleted(int i) {
		if (jsonValue == null) return false;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return false;
		return game.get("deleted").isBoolean().booleanValue();
	}
	
	public String getItemType(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		String typeValue =  game.get("type").isString().stringValue();
		typeValue = typeValue.substring(typeValue.lastIndexOf(".")+1);
		return typeValue;
	}
	
	public long getItemId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("id").isNumber().doubleValue();
	
	}
	
	public int getSortKey(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		if (!game.containsKey("sortKey")) return 0;
		return (int) game.get("sortKey").isNumber().doubleValue();
	}
	public long getGameId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("gameId").isNumber().doubleValue();
	}
}
