package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.GameBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.GameBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemDeserializer extends GameBeanDeserializer {

	@Override
	public GeneralItem toBean(JSONObject object) {
		GeneralItem gi = new GeneralItem();
		try {
			initBean(object, gi);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gi;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GeneralItem gi = (GeneralItem) genericBean;
		if (object.has("id")) gi.setId(object.getLong("id"));
		if (object.has("sortKey")) gi.setSortKey(object.getInt("sortKey"));
		if (object.has("scope")) gi.setScope(object.getString("scope"));
		if (object.has("name")) gi.setName(object.getString("name"));
		if (object.has("description")) gi.setDescription(object.getString("description"));
		if (object.has("iconUrl")) gi.setIconUrl(object.getString("iconUrl"));
		if (object.has("radius")) gi.setRadius(object.getInt("radius"));
		if (object.has("showAtTimeStamp")) gi.setShowAtTimeStamp(object.getLong("showAtTimeStamp"));
		if (object.has("lng")) gi.setLng(object.getDouble("lng"));
		if (object.has("lat")) gi.setLat(object.getDouble("lat"));
		if (object.has("autoLaunch")) gi.setAutoLaunch(object.getBoolean("autoLaunch"));
		if (object.has("dependsOn")) gi.setDependsOn((Dependency) JsonBeanDeserializer.deserialize(Dependency.class, object.getJSONObject("dependsOn")));
		if (object.has("disappearOn")) gi.setDisappearOn((Dependency) JsonBeanDeserializer.deserialize(Dependency.class, object.getJSONObject("disappearOn")));
		if (object.has("roles")) gi.setRoles(ListDeserializer.toStringList(object.getJSONArray("roles")));

	}

}
