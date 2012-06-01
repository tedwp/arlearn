package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameCreationStatusSerializer extends BeanSerializer{

	
	@Override
	public JSONObject toJSON(Object bean) {
		GameCreationStatus statusBean = (GameCreationStatus) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (statusBean.getGameId() != null) returnObject.put("gameId", statusBean.getGameId());
			if (statusBean.getStatus() != null) returnObject.put("status", statusBean.getStatus());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
