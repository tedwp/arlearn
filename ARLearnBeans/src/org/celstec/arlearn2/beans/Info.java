package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.NarratorItemDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItemDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItemSerializer;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.NarratorItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Info extends Bean {

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		Info other = (Info ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getVersion(), other.getVersion()); 
	}

	public static BeanDeserializer deserializer = new BeanDeserializer() {
//		public static class YoutubeObjectDeserializer extends NarratorItemDeserializer{

		@Override
		public Info toBean(JSONObject object) {
			Info bean = new Info();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Info bean = (Info) genericBean;
			if (object.has("version")) bean.setVersion(object.getString("youtubeUrl"));
		}
		
	};
	
	public static BeanSerializer serializer = new  BeanSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			Info info = (Info) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (info.getVersion() != null) returnObject.put("version", info.getVersion());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
