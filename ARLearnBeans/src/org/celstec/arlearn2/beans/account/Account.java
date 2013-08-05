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
package org.celstec.arlearn2.beans.account;


import java.util.StringTokenizer;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Account extends Bean {

	public final static int ADMINISTRATOR = 1;
	public final static int USER = 2;

	private String localId;	
	private Integer accountType;
	private String email;
	private String name;
	private String givenName;
	private String familyName;
	private String picture;
	private Integer accountLevel;

	
	public String getFullId() {
		return accountType+":"+localId;
	}
	
	public void setFullid(String accountName){
		StringTokenizer st = new StringTokenizer(accountName, ":");
		if (st.hasMoreTokens()) {
			setAccountType(Integer.parseInt(st.nextToken()));
		}
		if (st.hasMoreTokens()) {
			setLocalId(st.nextToken());
		}
	}
	
	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public Integer getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(Integer accountLevel) {
		this.accountLevel = accountLevel;
	}

	@Override
	public boolean equals(Object obj) {
		Account other = (Account ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getLocalId(), other.getLocalId()) && 
			nullSafeEquals(getAccountType(), other.getAccountType()) && 
			nullSafeEquals(getName(), other.getName()) && 
			nullSafeEquals(getGivenName(), other.getGivenName()) && 
			nullSafeEquals(getFamilyName(), other.getFamilyName()) && 
			nullSafeEquals(getPicture(), other.getPicture()); 
	}
	
	public static BeanDeserializer deserializer = new AccountDeserializer();
	
	public static class AccountDeserializer extends BeanDeserializer{

		@Override
		public Account toBean(JSONObject object) {
			Account bean = new Account();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Account bean = (Account) genericBean;
			if (object.has("localId")) bean.setLocalId(object.getString("localId"));
			if (object.has("accountType")) bean.setAccountType(object.getInt("accountType"));
			if (object.has("email")) bean.setEmail(object.getString("email"));
			if (object.has("name")) bean.setName(object.getString("name"));
			if (object.has("givenName")) bean.setGivenName(object.getString("givenName"));
			if (object.has("familyName")) bean.setFamilyName(object.getString("familyName"));
			if (object.has("picture")) bean.setPicture(object.getString("picture"));
			if (object.has("accountLevel")) bean.setAccountLevel(object.getInt("accountLevel"));
		}
	};
	

	
	public static BeanSerializer serializer = new AccountSerializer(); 
			
	public static class AccountSerializer	extends BeanSerializer  {

		@Override
		public JSONObject toJSON(Object bean) {
			Account accountBean = (Account) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (accountBean.getLocalId() != null) returnObject.put("localId", accountBean.getLocalId());
				if (accountBean.getAccountType() != null) returnObject.put("accountType", accountBean.getAccountType());
				if (accountBean.getEmail() != null) returnObject.put("email", accountBean.getEmail());
				if (accountBean.getName() != null) returnObject.put("name", accountBean.getName());
				if (accountBean.getGivenName() != null) returnObject.put("givenName", accountBean.getGivenName());
				if (accountBean.getFamilyName() != null) returnObject.put("familyName", accountBean.getFamilyName());
				if (accountBean.getPicture() != null) returnObject.put("picture", accountBean.getPicture());
				if (accountBean.getAccountLevel() != null) returnObject.put("accountLevel", accountBean.getAccountLevel());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

}
