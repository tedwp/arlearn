package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.GameBean;
import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameCreationStatusDeserializer extends BeanDeserializer{

	@Override
	public GameCreationStatus toBean(JSONObject object) {
		GameCreationStatus bean = new GameCreationStatus();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GameCreationStatus bean = (GameCreationStatus) genericBean;
		if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
		if (object.has("status")) bean.setStatus(object.getInt("status"));
	}

}
