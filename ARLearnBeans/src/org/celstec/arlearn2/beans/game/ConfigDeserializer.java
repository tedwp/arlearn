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
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ConfigDeserializer extends BeanDeserializer{
	
	@Override
	public Config toBean(JSONObject object) {
		Config returnObject = new Config();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Config oq = (Config) genericBean;
		if (object.has("scoring")) oq.setScoring(object.getBoolean("scoring"));
        if (object.has("messageViews")) oq.setMessageViews(object.getInt("messageViews"));
		if (object.has("mapAvailable")) oq.setMapAvailable(object.getBoolean("mapAvailable"));
		if (object.has("mapType")) oq.setMapType(object.getInt("mapType"));
		if (object.has("manualItems")) oq.setManualItems(ListDeserializer.toBean(object.getJSONArray("manualItems"), GeneralItem.class));
		if (object.has("locationUpdates")) oq.setLocationUpdates(ListDeserializer.toBean(object.getJSONArray("locationUpdates"), LocationUpdateConfig.class));
		if (object.has("roles")) oq.setRoles(ListDeserializer.toStringList(object.getJSONArray("roles")));
		if (object.has("mapRegions")) oq.setMapRegions(ListDeserializer.toBean(object.getJSONArray("mapRegions"), MapRegion.class));
        if (object.has("htmlMessageList")) oq.setHtmlMessageList(object.getString("htmlMessageList"));
	}
	
}
