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
package org.celstec.arlearn2.beans.run;

import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class User extends Account{

	private Long runId;
	private Boolean deleted;
	private String teamId;
	private List<String> roles;

	private Long gameId;
	private Long lastModificationDateGame;
	
	public User() {
		
	}
	
	public void setAccountData(Account data) {
		setEmail(data.getEmail());
		setName(data.getName());
		setGivenName(data.getGivenName());
		setFamilyName(data.getFamilyName());
		setPicture(data.getPicture());
		setAccountLevel(data.getAccountLevel());
	}
	
	@Override
	public boolean equals(Object obj) {
		User other = (User) obj;
		return super.equals(obj) && 
			nullSafeEquals(getDeleted(), other.getDeleted()) &&
			nullSafeEquals(getGameId(), other.getGameId()) &&
			nullSafeEquals(getRunId(), other.getRunId()) &&
			nullSafeEquals(getTeamId(), other.getTeamId()) &&
			nullSafeEquals(getRoles(), other.getRoles()) ; 
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public Boolean getDeleted() {
		if (deleted == null) return false;
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public static String normalizeEmail(String mail) {
		if (mail == null) return null;
		int posAt = mail.indexOf("@");
		if (posAt != -1) {
			mail = mail.substring(0, posAt);
		}
		return mail.replace(".", "").toLowerCase();
	}
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	public Long getLastModificationDateGame() {
		return lastModificationDateGame;
	}

	public void setLastModificationDateGame(Long lastModificationDateGame) {
		this.lastModificationDateGame = lastModificationDateGame;
	}
	
	public void setFullIdentifier(String id) {
		if (!id.contains(":")) return;
		setAccountType(Integer.parseInt(id.substring(0, id.indexOf(":"))));
		setLocalId(id.substring(id.indexOf(":")+1));
	}
	
	public static BeanDeserializer deserializer = new AccountDeserializer() {
		@Override
		public User toBean(JSONObject object) {
			User bean = new User();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			User bean = (User) genericBean;

			if (object.has("runId")) bean.setRunId(object.getLong("runId"));
			if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));
			if (object.has("teamId")) bean.setTeamId(object.getString("teamId"));
			if (object.has("roles")) bean.setRoles(ListDeserializer.toStringList(object.getJSONArray("roles")));
			if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
			if (object.has("lastModificationDateGame")) bean.setLastModificationDateGame(object.getLong("lastModificationDateGame"));


		}
	};
	
	public static BeanSerializer serializer = new AccountSerializer(){
		@Override
		public JSONObject toJSON(Object bean) {
			User runBean = (User) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (runBean.getRunId() != null) returnObject.put("runId", runBean.getRunId());
				if (runBean.getDeleted() != null) returnObject.put("deleted", runBean.getDeleted());
				if (runBean.getTeamId() != null) returnObject.put("teamId", runBean.getTeamId());
				if (runBean.getRoles() != null) returnObject.put("roles", ListSerializer.toStringList(runBean.getRoles()));
				if (runBean.getGameId() != null) returnObject.put("gameId", runBean.getGameId());
				if (runBean.getLastModificationDateGame() != null) returnObject.put("lastModificationDateGame", runBean.getLastModificationDateGame());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	}; 

	
}
