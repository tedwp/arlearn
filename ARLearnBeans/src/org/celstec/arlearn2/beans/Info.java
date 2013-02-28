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
package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
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
			if (object.has("version")) bean.setVersion(object.getString("version"));
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
