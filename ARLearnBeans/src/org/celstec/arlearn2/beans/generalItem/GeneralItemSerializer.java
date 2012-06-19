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
			if (gi.getRadius() != null) returnObject.put("radius", gi.getRadius());
			if (gi.getShowAtTimeStamp() != null) returnObject.put("showAtTimeStamp", gi.getShowAtTimeStamp());
			if (gi.getLng() != null) returnObject.put("lng", gi.getLng());
			if (gi.getLat() != null) returnObject.put("lat", gi.getLat());
			if (gi.getIconUrl() != null) returnObject.put("iconUrl", gi.getIconUrl());
			if (gi.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(gi.getRoles()));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
