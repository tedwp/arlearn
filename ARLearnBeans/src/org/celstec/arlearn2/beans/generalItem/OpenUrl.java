package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenUrl extends GeneralItem {
	
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object obj) {
		OpenUrl other = (OpenUrl ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getUrl(), other.getUrl()); 
	}
	

	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			OpenUrl ou = (OpenUrl) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (ou.getUrl() != null) returnObject.put("url", ou.getUrl());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){
		@Override
		public GeneralItem toBean(JSONObject object) {
			OpenUrl gi = new OpenUrl();
			try {
				initBean(object, gi);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return gi;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			OpenUrl gi = (OpenUrl) genericBean;
			if (object.has("url")) gi.setUrl(object.getString("url"));
		}

	};

}
