package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.game.Game;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameSerializer extends GameBeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		Game game = (Game) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (game.getTitle() != null) returnObject.put("title", game.getTitle());
			if (game.getCreator() != null) returnObject.put("creator", game.getCreator());
			if (game.getOwner() != null) returnObject.put("owner", game.getOwner());
			if (game.getFeedUrl() != null) returnObject.put("feedUrl", game.getFeedUrl());
			if (game.getConfig() != null) returnObject.put("config", JsonBeanSerialiser.serialiseToJson(game.getConfig()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
