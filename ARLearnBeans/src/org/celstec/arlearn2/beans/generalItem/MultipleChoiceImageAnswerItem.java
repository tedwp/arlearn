package org.celstec.arlearn2.beans.generalItem;


import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.MultipleChoiceAnswerItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.MultipleChoiceAnswerItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceImageAnswerItem extends MultipleChoiceAnswerItem{
	
	private String imageUrl;
	private String audioUrl;

	public MultipleChoiceImageAnswerItem() {
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public boolean equals(Object obj) {
		MultipleChoiceImageAnswerItem other = (MultipleChoiceImageAnswerItem ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getImageUrl(), other.getImageUrl()); 
	}
	
	public static MultipleChoiceAnswerItemDeserializer deserializer = new MultipleChoiceAnswerItemDeserializer() {

		@Override
		public MultipleChoiceImageAnswerItem toBean(JSONObject object) {
			MultipleChoiceImageAnswerItem bean = new MultipleChoiceImageAnswerItem();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			MultipleChoiceImageAnswerItem bean = (MultipleChoiceImageAnswerItem) genericBean;
			if (object.has("imageUrl")) bean.setImageUrl(object.getString("imageUrl"));
			if (object.has("audioUrl")) bean.setAudioUrl(object.getString("audioUrl"));
		}
		
	};
	
	public static MultipleChoiceAnswerItemSerializer serializer = new  MultipleChoiceAnswerItemSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			MultipleChoiceImageAnswerItem item = (MultipleChoiceImageAnswerItem) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (item.getImageUrl() != null) returnObject.put("imageUrl", item.getImageUrl());
				if (item.getAudioUrl() != null) returnObject.put("audioUrl", item.getAudioUrl());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	

}
