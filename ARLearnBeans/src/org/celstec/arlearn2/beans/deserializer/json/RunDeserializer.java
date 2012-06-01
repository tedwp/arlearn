package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunDeserializer extends RunBeanDeserializer{

	@Override
	public Run toBean(JSONObject object) {
		Run bean = new Run();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Run bean = (Run) genericBean;
		if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));	
		if (object.has("title")) bean.setTitle(object.getString("title"));	
		if (object.has("owner")) bean.setOwner(object.getString("owner"));
		if (object.has("startTime")) bean.setStartTime(object.getLong("startTime"));
		if (object.has("serverCreationTime")) bean.setServerCreationTime(object.getLong("serverCreationTime"));
		if (object.has("game")) bean.setGame((Game) JsonBeanDeserializer.deserialize(Game.class, object.getJSONObject("game")));
	}
}
