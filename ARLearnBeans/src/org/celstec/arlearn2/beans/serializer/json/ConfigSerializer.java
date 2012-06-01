package org.celstec.arlearn2.beans.serializer.json;


import org.celstec.arlearn2.beans.game.Config;
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
			if (c.getMapAvailable()!= null) returnObject.put("mapAvailable", c.getMapAvailable());
			if (c.getManualItems() != null) returnObject.put("manualItems", ListSerializer.toJSON(c.getManualItems()));
			if (c.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(c.getRoles()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
