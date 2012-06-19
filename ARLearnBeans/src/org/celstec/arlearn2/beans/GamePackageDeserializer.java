package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamePackageDeserializer extends BeanDeserializer{

	@Override
	public GamePackage toBean(JSONObject object) {
		GamePackage bean = new GamePackage();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GamePackage bean = (GamePackage) genericBean;
		if (object.has("game")) bean.setGame((Game) JsonBeanDeserializer.deserialize(Game.class, object.getJSONObject("game")));
		if (object.has("generalItems")) bean.setGeneralItems(ListDeserializer.toBean(object.getJSONArray("generalItems"), GeneralItem.class));
		if (object.has("scoreDefinitions")) bean.setScoreDefinitions(ListDeserializer.toBean(object.getJSONArray("scoreDefinitions"), ScoreDefinition.class));
	}

}
