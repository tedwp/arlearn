package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamesListDeserializer  extends BeanDeserializer{

	@Override
	public GamesList toBean(JSONObject object) {
		GamesList tl = new GamesList();
		try {
			initBean(object, tl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tl;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GamesList gamesList = (GamesList) genericBean;
		if (object.has("serverTime")) gamesList.setServerTime(object.getLong("serverTime"));
		if (object.has("games")) gamesList.setGames(ListDeserializer.toBean(object.getJSONArray("games"), Game.class));
	}

}
