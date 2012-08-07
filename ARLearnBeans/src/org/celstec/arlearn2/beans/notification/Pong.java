package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Pong extends Ping{
	
	private Long origTimeStamp;
	private String response;

	public Long getOrigTimeStamp() {
		return origTimeStamp;
	}

	public void setOrigTimeStamp(Long origTimeStamp) {
		this.origTimeStamp = origTimeStamp;
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public boolean equals(Object obj) {
		Pong other = (Pong ) obj;
		return super.equals(obj) && 
		nullSafeEquals(getResponse(), other.getResponse()) && 
		nullSafeEquals(getOrigTimeStamp(), other.getOrigTimeStamp()); 

	}
	
	public static class Deserializer extends Ping.Deserializer{

		@Override
		public Pong toBean(JSONObject object) {
			Pong bean = new Pong();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Pong bean = (Pong) genericBean;
			if (object.has("origTimeStamp")) bean.setOrigTimeStamp(object.getLong("origTimeStamp"));
			if (object.has("response")) bean.setResponse(object.getString("response"));
		}

	}
	
	public static class Serializer extends Ping.Serializer{

		@Override
		public JSONObject toJSON(Object bean) {
			Pong statusBean = (Pong) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getOrigTimeStamp() != null) returnObject.put("origTimeStamp", statusBean.getOrigTimeStamp());
				if (statusBean.getResponse() != null) returnObject.put("response", statusBean.getResponse());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
}
