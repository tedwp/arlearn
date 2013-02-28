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
package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamePackageDeserializer extends BeanDeserializer{

	@Override
	public GamePackage toBean(JSONObject object) {
		GamePackage bean = new GamePackage();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GamePackage bean = (GamePackage) genericBean;
		if (object.has("game")) bean.setGame((Game) JsonBeanDeserializer.deserialize(Game.class, object.getJSONObject("game")));
		if (object.has("generalItems")) bean.setGeneralItems(ListDeserializer.toBean(object.getJSONArray("generalItems"), GeneralItem.class));
		if (object.has("scoreDefinitions")) bean.setScoreDefinitions(ListDeserializer.toBean(object.getJSONArray("scoreDefinitions"), ScoreDefinition.class));
	}

}
