package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.GameBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameDeserializer extends GameBeanDeserializer {

	@Override
	public Game toBean(JSONObject object) {
		Game returnObject = new Game();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Game g = (Game) genericBean;
		if (object.has("title")) g.setTitle(object.getString("title"));
		if (object.has("creator")) g.setCreator(object.getString("creator"));
		if (object.has("owner")) g.setOwner(object.getString("owner"));
		if (object.has("feedUrl")) g.setFeedUrl(object.getString("feedUrl"));
		if (object.has("config")) g.setConfig((Config) JsonBeanDeserializer.deserialize(Config.class, object.getJSONObject("config")));

	}
}
