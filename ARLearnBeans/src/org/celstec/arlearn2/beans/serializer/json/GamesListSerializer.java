package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.game.GamesList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamesListSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		GamesList gamesList = (GamesList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gamesList.getServerTime() != null) returnObject.put("serverTime", gamesList.getServerTime());
			if (gamesList.getGames() != null) returnObject.put("games", ListSerializer.toJSON(gamesList.getGames()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}


}
