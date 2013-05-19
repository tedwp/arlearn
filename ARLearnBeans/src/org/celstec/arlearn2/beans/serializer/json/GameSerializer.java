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
package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.game.Game;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameSerializer extends GameBeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		Game game = (Game) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (game.getTitle() != null) returnObject.put("title", game.getTitle());
			if (game.getCreator() != null) returnObject.put("creator", game.getCreator());
			if (game.getDescription() != null) returnObject.put("description", game.getDescription());
			if (game.getOwner() != null) returnObject.put("owner", game.getOwner());
			if (game.getFeedUrl() != null) returnObject.put("feedUrl", game.getFeedUrl());
			if (game.getSharing() != null) returnObject.put("sharing", game.getSharing());
			if (game.getLicenseCode() != null) returnObject.put("licenseCode", game.getLicenseCode());
			if (game.getConfig() != null) returnObject.put("config", JsonBeanSerialiser.serialiseToJson(game.getConfig()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
