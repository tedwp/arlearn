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
package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Location extends RunBean{

	private String account;
	private Double lng;
	private Double lat;
	private Float accuracy;
	
	public Location() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

	public static class Deserializer extends RunBeanDeserializer{

		@Override
		public Location toBean(JSONObject object) {
			Location bean = new Location();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Location bean = (Location) genericBean;
			if (object.has("account")) bean.setAccount(object.getString("from"));
			if (object.has("lat")) bean.setLat(object.getDouble("lat"));
			if (object.has("lng")) bean.setLng(object.getDouble("lng"));
			if (object.has("accuracy")) bean.setLng(object.getDouble("accuracy"));
		}

	}
	
	public static class Serializer extends BeanSerializer{

		@Override
		public JSONObject toJSON(Object bean) {
			Location statusBean = (Location) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getAccount() != null) returnObject.put("account", statusBean.getAccount());
				if (statusBean.getLat() != null) returnObject.put("lat", statusBean.getLat());
				if (statusBean.getLng() != null) returnObject.put("lng", statusBean.getLng());
				if (statusBean.getAccuracy() != null) returnObject.put("accuracy", statusBean.getAccuracy());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
	
}
