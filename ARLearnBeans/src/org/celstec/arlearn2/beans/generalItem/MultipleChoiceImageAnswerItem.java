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
package org.celstec.arlearn2.beans.generalItem;


import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.MultipleChoiceAnswerItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.MultipleChoiceAnswerItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceImageAnswerItem extends MultipleChoiceAnswerItem{

    private String label;
    private String imageUrl;
    private String imageMd5Hash;
	private String audioUrl;
    private String audioMd5Hash;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageMd5Hash() {
        return imageMd5Hash;
    }

    public void setImageMd5Hash(String imageMd5Hash) {
        this.imageMd5Hash = imageMd5Hash;
    }

    public String getAudioMd5Hash() {
        return audioMd5Hash;
    }

    public void setAudioMd5Hash(String audioMd5Hash) {
        this.audioMd5Hash = audioMd5Hash;
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

            if (object.has("label")) bean.setLabel(object.getString("label"));
            if (object.has("imageMd5Hash")) bean.setImageMd5Hash(object.getString("imageMd5Hash"));
            if (object.has("audioMd5Hash")) bean.setAudioMd5Hash(object.getString("audioMd5Hash"));
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

                if (item.getLabel() != null) returnObject.put("label", item.getLabel());
                if (item.getImageMd5Hash() != null) returnObject.put("imageMd5Hash", item.getImageMd5Hash());
                if (item.getAudioMd5Hash() != null) returnObject.put("audioMd5Hash", item.getAudioMd5Hash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	

}
