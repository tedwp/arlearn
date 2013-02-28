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
package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.beans.run.RunConfig;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Pong extends Ping{
	
	private Long origTimeStamp;
	private Location location;
	private Integer requestType;
	private String response;

	public Long getOrigTimeStamp() {
		return origTimeStamp;
	}

	public void setOrigTimeStamp(Long origTimeStamp) {
		this.origTimeStamp = origTimeStamp;
	}
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
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
		nullSafeEquals(getRequestType(), other.getRequestType()) && 
		nullSafeEquals(getResponse(), other.getResponse()) && 
		nullSafeEquals(getOrigTimeStamp(), other.getOrigTimeStamp()) &&
		nullSafeEquals(getLocation(), other.getLocation()); 

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
			if (object.has("requestType")) bean.setRequestType(object.getInt("requestType"));
			if (object.has("origTimeStamp")) bean.setOrigTimeStamp(object.getLong("origTimeStamp"));
			if (object.has("response")) bean.setResponse(object.getString("response"));
			if (object.has("location")) bean.setLocation((Location) JsonBeanDeserializer.deserialize(Location.class, object.getJSONObject("location")));

		}

	}
	
	public static class Serializer extends Ping.Serializer{

		@Override
		public JSONObject toJSON(Object bean) {
			Pong statusBean = (Pong) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getRequestType() != null) returnObject.put("requestType", statusBean.getRequestType());
				if (statusBean.getOrigTimeStamp() != null) returnObject.put("origTimeStamp", statusBean.getOrigTimeStamp());
				if (statusBean.getResponse() != null) returnObject.put("response", statusBean.getResponse());
				if (statusBean.getLocation() != null) returnObject.put("location", JsonBeanSerialiser.serialiseToJson(statusBean.getLocation()));

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
}
