package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameModification extends Bean{
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	private Integer modificationType;
	
	private Game game;
	
	public GameModification(){}

	public Integer getModificationType() {
		return modificationType;
	}

	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public boolean equals(Object obj) {
		GameModification other = (GameModification ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGame(), other.getGame()) && 
			nullSafeEquals(getModificationType(), other.getModificationType()); 
	}
	
	public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public GameModification toBean(JSONObject object) {
			GameModification bean = new GameModification();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			GameModification bean = (GameModification) genericBean;
			if (object.has("modificationType")) bean.setModificationType(object.getInt("modificationType"));
			if (object.has("game")) bean.setGame((Game) JsonBeanDeserializer.deserialize(Game.class, object.getJSONObject("game")));

		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			GameModification gameBean = (GameModification) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (gameBean.getModificationType() != null) returnObject.put("modificationType", gameBean.getModificationType());
				if (gameBean.getGame() != null) returnObject.put("game", JsonBeanSerialiser.serialiseToJson(gameBean.getGame()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

}
