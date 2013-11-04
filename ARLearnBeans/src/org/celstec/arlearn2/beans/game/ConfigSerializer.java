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
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ConfigSerializer extends BeanSerializer {

//	private List<GeneralItem> manualItems = new ArrayList<GeneralItem>();
	
	@Override
	public JSONObject toJSON(Object bean) {
		Config c = (Config) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (c.getScoring()!= null) returnObject.put("scoring", c.getScoring());
            if (c.getMessageViews()!= null) returnObject.put("messageViews", c.getMessageViews());
			if (c.getMapAvailable()!= null) returnObject.put("mapAvailable", c.getMapAvailable());
			if (c.getMapType()!= null) returnObject.put("mapType", c.getMapType());
			if (c.getManualItems() != null) returnObject.put("manualItems", ListSerializer.toJSON(c.getManualItems()));
			if (c.getLocationUpdates() != null) returnObject.put("locationUpdates", ListSerializer.toJSON(c.getLocationUpdates()));
			if (c.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(c.getRoles()));
			if (c.getMapRegions() != null) returnObject.put("mapRegions", ListSerializer.toJSON(c.getMapRegions()));
            if (c.getHtmlMessageList()!= null) returnObject.put("htmlMessageList", c.getHtmlMessageList());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
