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
import org.celstec.arlearn2.beans.deserializer.json.NarratorItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.NarratorItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class YoutubeObject extends NarratorItem{

	private String youtubeUrl;
	
	public YoutubeObject() {
		
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}
	
	public static GeneralItemDeserializer deserializer = new NarratorItemDeserializer() {
//		public static class YoutubeObjectDeserializer extends NarratorItemDeserializer{

		@Override
		public YoutubeObject toBean(JSONObject object) {
			YoutubeObject bean = new YoutubeObject();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			YoutubeObject bean = (YoutubeObject) genericBean;
			if (object.has("youtubeUrl")) bean.setYoutubeUrl(object.getString("youtubeUrl"));
		}
		
	};
	
	public static GeneralItemSerializer serializer = new  NarratorItemSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			YoutubeObject yo = (YoutubeObject) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (yo.getYoutubeUrl() != null) returnObject.put("youtubeUrl", yo.getYoutubeUrl());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

}
