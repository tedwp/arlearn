package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GCMDeviceDescription extends DeviceDescription {

	private String account;
	private String deviceUniqueIdentifier;
	private String registrationId;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public String getDeviceUniqueIdentifier() {
		return deviceUniqueIdentifier;
	}
	public void setDeviceUniqueIdentifier(String deviceUniqueIdentifier) {
		this.deviceUniqueIdentifier = deviceUniqueIdentifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		GCMDeviceDescription other = (GCMDeviceDescription ) obj;
		return super.equals(obj) && 
		nullSafeEquals(getAccount(), other.getAccount()) && 
		nullSafeEquals(getDeviceUniqueIdentifier(), other.getDeviceUniqueIdentifier()) && 
		nullSafeEquals(getRegistrationId(), other.getRegistrationId()); 
	}
	
	public static class Deserializer extends BeanDeserializer{

		@Override
		public GCMDeviceDescription toBean(JSONObject object) {
			GCMDeviceDescription bean = new GCMDeviceDescription();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			GCMDeviceDescription bean = (GCMDeviceDescription) genericBean;
			if (object.has("account")) bean.setAccount(object.getString("account"));
			if (object.has("deviceUniqueIdentifier")) bean.setDeviceUniqueIdentifier(object.getString("deviceUniqueIdentifier"));
			if (object.has("registrationId")) bean.setRegistrationId(object.getString("registrationId"));
		}

	}
	
	public static class Serializer extends BeanSerializer{

		@Override
		public JSONObject toJSON(Object bean) {
			GCMDeviceDescription statusBean = (GCMDeviceDescription) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getAccount() != null) returnObject.put("account", statusBean.getAccount());
				if (statusBean.getDeviceUniqueIdentifier() != null) returnObject.put("deviceUniqueIdentifier", statusBean.getDeviceUniqueIdentifier());
				if (statusBean.getRegistrationId() != null) returnObject.put("registrationId", statusBean.getRegistrationId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
	
}
