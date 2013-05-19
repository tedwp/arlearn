package org.celstec.arlearn2.beans.oauth;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OauthInfo extends Bean {

	private String clientId;
	private Integer providerId;
	private String redirectUri;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	@Override
	public boolean equals(Object obj) {
		OauthInfo other = (OauthInfo ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getRedirectUri(), other.getRedirectUri()) &&
			nullSafeEquals(getClientId(), other.getClientId()) &&
			nullSafeEquals(getProviderId(), other.getProviderId()) ; 

	}
	
	public static BeanDeserializer deserializer = new  BeanDeserializer() {

		@Override
		public OauthInfo toBean(JSONObject object) {
			OauthInfo bean = new OauthInfo();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			OauthInfo bean = (OauthInfo) genericBean;
			if (object.has("clientId")) bean.setClientId(object.getString("clientId"));
			if (object.has("providerId")) bean.setProviderId(object.getInt("providerId"));
			if (object.has("redirectUri")) bean.setRedirectUri(object.getString("redirectUri"));
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer(){
		@Override
		public JSONObject toJSON(Object bean) {
			OauthInfo oauth = (OauthInfo) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (oauth.getClientId() != null) returnObject.put("clientId", oauth.getClientId());
				if (oauth.getProviderId() != null) returnObject.put("providerId", oauth.getProviderId());
				if (oauth.getRedirectUri() != null) returnObject.put("redirectUri", oauth.getRedirectUri());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
