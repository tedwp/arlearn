package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.GameBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameBeanDeserializer extends BeanDeserializer{

	@Override
	public GameBean toBean(JSONObject object) {
		GameBean bean = new GameBean();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GameBean bean = (GameBean) genericBean;
		if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
		if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));
		if (object.has("lastModificationDate")) bean.setLastModificationDate(object.getLong("lastModificationDate"));

		
	}
}
