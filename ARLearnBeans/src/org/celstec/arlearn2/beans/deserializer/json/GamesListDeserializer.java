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
		GamesList teamlist = (GamesList) genericBean;
		if (object.has("games")) teamlist.setGames(ListDeserializer.toBean(object.getJSONArray("games"), Game.class));
	}

}
