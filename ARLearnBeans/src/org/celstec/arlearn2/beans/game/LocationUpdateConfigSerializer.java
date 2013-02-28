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
package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateConfigSerializer  extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		LocationUpdateConfig luc = (LocationUpdateConfig) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (luc.getDelay()!= null) returnObject.put("delay", luc.getDelay());
			if (luc.getTimeBetweenUpdates()!= null) returnObject.put("timeBetweenUpdates", luc.getTimeBetweenUpdates());
			if (luc.getRole()!= null) returnObject.put("role", luc.getRole());
			if (luc.getScope()!= null) returnObject.put("scope", luc.getScope());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
