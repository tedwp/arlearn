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
