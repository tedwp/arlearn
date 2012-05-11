package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Config;
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
		if (object.has("mapAvailable")) oq.setMapAvailable(object.getBoolean("mapAvailable"));
		if (object.has("manualItems")) oq.setManualItems(ListDeserializer.toBean(object.getJSONArray("manualItems"), GeneralItem.class));
		if (object.has("roles")) oq.setRoles(ListDeserializer.toStringList(object.getJSONArray("roles")));
	}
	
}