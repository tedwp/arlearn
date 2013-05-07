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
package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.GameBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameDeserializer extends GameBeanDeserializer {

	@Override
	public Game toBean(JSONObject object) {
		Game returnObject = new Game();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Game g = (Game) genericBean;
		if (object.has("title")) g.setTitle(object.getString("title"));
		if (object.has("creator")) g.setCreator(object.getString("creator"));
		if (object.has("description")) g.setDescription(object.getString("description"));
		if (object.has("owner")) g.setOwner(object.getString("owner"));
		if (object.has("feedUrl")) g.setFeedUrl(object.getString("feedUrl"));
		if (object.has("sharing")) g.setSharing(object.getInt("sharing"));
		if (object.has("config")) g.setConfig((Config) JsonBeanDeserializer.deserialize(Config.class, object.getJSONObject("config")));

	}
}
