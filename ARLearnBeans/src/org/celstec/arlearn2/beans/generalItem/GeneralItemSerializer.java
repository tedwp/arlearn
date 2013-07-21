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

import org.celstec.arlearn2.beans.serializer.json.GameBeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemSerializer extends GameBeanSerializer{

	
	@Override
	public JSONObject toJSON(Object bean) {
		GeneralItem gi = (GeneralItem) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gi.getId() != null) returnObject.put("id", gi.getId());
			if (gi.getSortKey() != null) returnObject.put("sortKey", gi.getSortKey());
			if (gi.getScope() != null) returnObject.put("scope", gi.getScope());
			if (gi.getName() != null) returnObject.put("name", gi.getName());
			if (gi.getDescription() != null) returnObject.put("description", gi.getDescription());
			if (gi.getDependsOn() != null) returnObject.put("dependsOn", JsonBeanSerialiser.serialiseToJson(gi.getDependsOn()));
			if (gi.getDisappearOn() != null) returnObject.put("disappearOn", JsonBeanSerialiser.serialiseToJson(gi.getDisappearOn()));
			if (gi.getRadius() != null) returnObject.put("radius", gi.getRadius());
//			if (gi.getShowAtTimeStamp() != null) returnObject.put("showAtTimeStamp", gi.getShowAtTimeStamp());
			if (gi.getLng() != null) returnObject.put("lng", gi.getLng());
			if (gi.getLat() != null) returnObject.put("lat", gi.getLat());
			if (gi.getVisibleAt() != null) returnObject.put("visibleAt", gi.getVisibleAt());
			if (gi.getDisappearAt() != null) returnObject.put("disappearAt", gi.getDisappearAt());
			if (gi.getAutoLaunch() != null) returnObject.put("autoLaunch", gi.getAutoLaunch());
			if (gi.getShowCountDown() != null) returnObject.put("showCountDown", gi.getShowCountDown());
			if (gi.getIconUrl() != null) returnObject.put("iconUrl", gi.getIconUrl());
            if (gi.getSection() != null) returnObject.put("section", gi.getSection());
			if (gi.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(gi.getRoles()));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
