/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameModification extends NotificationBean{
	
	
	public final static int CREATED = GAME_CREATED;
	public final static int DELETED = GAME_DELETED;
	public final static int ALTERED = GAME_ALTERED;
	
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
