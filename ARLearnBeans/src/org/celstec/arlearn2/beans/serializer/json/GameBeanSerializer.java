package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.game.GameBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameBeanSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		GameBean gameBean = (GameBean) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gameBean.getGameId() != null) returnObject.put("gameId", gameBean.getGameId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
