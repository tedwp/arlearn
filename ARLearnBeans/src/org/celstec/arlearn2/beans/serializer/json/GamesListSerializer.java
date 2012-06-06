package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.game.GamesList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamesListSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		GamesList tl = (GamesList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (tl.getGames() != null) returnObject.put("games", ListSerializer.toJSON(tl.getGames()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}


}
