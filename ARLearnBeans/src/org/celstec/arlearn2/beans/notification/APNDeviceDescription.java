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
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class APNDeviceDescription extends DeviceDescription {

	private String account;
	private String deviceUniqueIdentifier;
	private String deviceToken;
    private String bundleIdentifier;
	
	
//	{
//		
//		"account":"arlearn1",
//		"deviceUniqueIdentifier":"deviceUniqueIdentifier",
//		"deviceToken","deviceToken"
//	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDeviceUniqueIdentifier() {
		return deviceUniqueIdentifier;
	}
	public void setDeviceUniqueIdentifier(String deviceUniqueIdentifier) {
		this.deviceUniqueIdentifier = deviceUniqueIdentifier;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

    public String getBundleIdentifier() {
        return bundleIdentifier;
    }

    public void setBundleIdentifier(String bundleIdentifier) {
        this.bundleIdentifier = bundleIdentifier;
    }

    @Override
	public boolean equals(Object obj) {
		APNDeviceDescription other = (APNDeviceDescription ) obj;
		return super.equals(obj) && 
		nullSafeEquals(getAccount(), other.getAccount()) && 
		nullSafeEquals(getDeviceUniqueIdentifier(), other.getDeviceUniqueIdentifier()) && 
		nullSafeEquals(getDeviceToken(), other.getDeviceToken())&&
        nullSafeEquals(getBundleIdentifier(), other.getBundleIdentifier());
    }
	
	public static class Deserializer extends BeanDeserializer{

		@Override
		public APNDeviceDescription toBean(JSONObject object) {
			APNDeviceDescription bean = new APNDeviceDescription();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			APNDeviceDescription bean = (APNDeviceDescription) genericBean;
			if (object.has("account")) bean.setAccount(object.getString("account"));
			if (object.has("deviceUniqueIdentifier")) bean.setDeviceUniqueIdentifier(object.getString("deviceUniqueIdentifier"));
			if (object.has("deviceToken")) bean.setDeviceToken(object.getString("deviceToken"));
            if (object.has("bundleIdentifier")) bean.setBundleIdentifier(object.getString("bundleIdentifier"));
		}

	}
	
	public static class Serializer extends BeanSerializer{

		@Override
		public JSONObject toJSON(Object bean) {
			APNDeviceDescription statusBean = (APNDeviceDescription) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getAccount() != null) returnObject.put("account", statusBean.getAccount());
				if (statusBean.getDeviceUniqueIdentifier() != null) returnObject.put("deviceUniqueIdentifier", statusBean.getDeviceUniqueIdentifier());
				if (statusBean.getDeviceToken() != null) returnObject.put("deviceToken", statusBean.getDeviceToken());
                if (statusBean.getBundleIdentifier() != null) returnObject.put("bundleIdentifier", statusBean.getBundleIdentifier());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
	
}
