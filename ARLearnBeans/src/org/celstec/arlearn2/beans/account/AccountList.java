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

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class AccountList extends Bean{

	public static String AccountType = "org.celstec.arlearn2.beans.account.Account";

	private List<Account> accountList = new ArrayList<Account>();
	private Long serverTime;
	private String resumptionToken;

	public AccountList() {

	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	
	public void addAccount(Account account) {
		this.accountList.add(account);
	}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}
	
	public static BeanSerializer serializer = new BeanSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			AccountList accountList = (AccountList) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (accountList.getServerTime() != null)
					returnObject.put("serverTime", accountList.getServerTime());
				if (accountList.getResumptionToken() != null) returnObject.put("resumptionToken", accountList.getResumptionToken());

				if (accountList.getAccountList() != null)
					returnObject.put("accountList", ListSerializer.toJSON(accountList.getAccountList()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	public static BeanDeserializer deserializer = new BeanDeserializer() {

		@Override
		public AccountList toBean(JSONObject object) {
			AccountList al = new AccountList();
			try {
				initBean(object, al);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return al;
		}

		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			AccountList accountList = (AccountList) genericBean;
			if (object.has("serverTime"))
				accountList.setServerTime(object.getLong("serverTime"));
			if (object.has("resumptionToken")) accountList.setResumptionToken(object.getString("resumptionToken"));
			if (object.has("accountList"))
				accountList.setAccountList(ListDeserializer.toBean(object.getJSONArray("accountList"), Account.class));
		}
	};

}
