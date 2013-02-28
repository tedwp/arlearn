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

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamePackageSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		GamePackage gamePackage = (GamePackage) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gamePackage.getGame() != null) returnObject.put("game", JsonBeanSerialiser.serialiseToJson(gamePackage.getGame()));
			if (gamePackage.getGeneralItems() != null) returnObject.put("generalItems", ListSerializer.toJSON(gamePackage.getGeneralItems()));
			if (gamePackage.getScoreDefinitions() != null) returnObject.put("scoreDefinitions", ListSerializer.toJSON(gamePackage.getScoreDefinitions()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
