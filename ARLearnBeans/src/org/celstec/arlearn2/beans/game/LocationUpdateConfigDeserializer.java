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

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LocationUpdateConfigDeserializer extends BeanDeserializer{
	
	@Override
	public LocationUpdateConfig toBean(JSONObject object) {
		LocationUpdateConfig returnObject = new LocationUpdateConfig();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		LocationUpdateConfig luc = (LocationUpdateConfig) genericBean;
		if (object.has("delay")) luc.setDelay(object.getLong("delay"));
		if (object.has("timeBetweenUpdates")) luc.setTimeBetweenUpdates(object.getLong("timeBetweenUpdates"));
		if (object.has("role")) luc.setRole(object.getString("role"));
		if (object.has("scope")) luc.setScope(object.getString("scope"));
		
	}

}
