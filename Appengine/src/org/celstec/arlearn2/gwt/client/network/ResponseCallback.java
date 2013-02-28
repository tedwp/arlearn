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

@Deprecated
public abstract class ResponseCallback extends JsonCallback {

	private JSONArray jsonValue;

	@Override
	public void onJsonReceived(JSONValue jsonValue) {
		if (jsonValue.isObject().get("responses") != null
				&& jsonValue.isObject().get("responses").isArray() != null) {
			this.jsonValue = jsonValue.isObject().get("responses").isArray();
		}
		onResponsesReady();
	}

	@Override
	public abstract void onError();

	public abstract void onResponsesReady();

	public int responsesSize() {
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public long getGeneralItemId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		if (!game.containsKey("generalItemId")) return -1;
		return (long) game.get("generalItemId").isNumber().doubleValue();
	}
	
	public String getUserEmail(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("userEmail").isString().stringValue();
	}
	
	public String getResponseValue(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		return game.get("responseValue").isString().stringValue();
	}
	
	public long getRunId(int i) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
		return (long) game.get("runId").isNumber().doubleValue();
	}
	
}
