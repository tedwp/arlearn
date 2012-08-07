package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Ping extends Bean {
	
	public static final int DB_QUERY = 1;
	public static final int READ_NFC = 2;
	
	private String from;
	private String to;
	
	private Integer requestType;
	private String payload;

	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	

	

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object obj) {
		Ping other = (Ping ) obj;
		return super.equals(obj) && 
		nullSafeEquals(getRequestType(), other.getRequestType()) && 
		nullSafeEquals(getPayload(), other.getPayload()) && 
		nullSafeEquals(getFrom(), other.getFrom()) && 
		nullSafeEquals(getFrom(), other.getFrom()); 

	}
	
	public static class Deserializer extends BeanDeserializer{

		@Override
		public Ping toBean(JSONObject object) {
			Ping bean = new Ping();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Ping bean = (Ping) genericBean;
			if (object.has("from")) bean.setFrom(object.getString("from"));
			if (object.has("to")) bean.setTo(object.getString("to"));
			if (object.has("requestType")) bean.setRequestType(object.getInt("requestType"));
			if (object.has("payload")) bean.setPayload(object.getString("payload"));
		}

	}
	
	public static class Serializer extends BeanSerializer{

		@Override
		public JSONObject toJSON(Object bean) {
			Ping statusBean = (Ping) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getFrom() != null) returnObject.put("from", statusBean.getFrom());
				if (statusBean.getTo() != null) returnObject.put("to", statusBean.getTo());
				if (statusBean.getRequestType() != null) returnObject.put("requestType", statusBean.getRequestType());
				if (statusBean.getPayload() != null) returnObject.put("payload", statusBean.getPayload());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}

}
