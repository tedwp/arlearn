package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GameAccess extends Bean{
	
	private String account;
	private Integer accessRights;
	private Long gameId;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(Integer accessRights) {
		this.accessRights = accessRights;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public GameAccess toBean(JSONObject object) {
			GameAccess bean = new GameAccess();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			GameAccess bean = (GameAccess) genericBean;
			if (object.has("account")) bean.setAccount(object.getString("account"));
			if (object.has("accessRights")) bean.setAccessRights(object.getInt("accessRights"));
			if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			GameAccess teamBean = (GameAccess) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (teamBean.getAccount() != null) returnObject.put("account", teamBean.getAccount());
				if (teamBean.getAccessRights() != null) returnObject.put("accessRights", teamBean.getAccessRights());
				if (teamBean.getGameId() != null) returnObject.put("gameId", teamBean.getGameId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
