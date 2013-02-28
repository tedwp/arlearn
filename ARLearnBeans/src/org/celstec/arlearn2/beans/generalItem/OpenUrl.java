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
		if (!super.equals(obj)) return false;
		OpenUrl other = (OpenUrl ) obj;
		return 
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
