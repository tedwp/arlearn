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
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemVisibility extends RunBean {

	private Long generalItemId;
	private String email;
	private Integer status;
	private Long timeStamp;
	private Long lastModificationDate;
    
    public GeneralItemVisibility() {}

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public static RunBeanSerialiser serializer = new RunBeanSerialiser(){

		@Override
		public JSONObject toJSON(Object bean) {
			GeneralItemVisibility giv = (GeneralItemVisibility) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (giv.getLastModificationDate() != null) returnObject.put("lastModificationDate", giv.getLastModificationDate());
				if (giv.getTimeStamp() != null) returnObject.put("timeStamp", giv.getTimeStamp());
				if (giv.getStatus() != null) returnObject.put("status", giv.getStatus());
				if (giv.getEmail() != null) returnObject.put("email", giv.getEmail());
				if (giv.getGeneralItemId() != null) returnObject.put("generalItemId", giv.getGeneralItemId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static RunBeanDeserializer deserializer = new RunBeanDeserializer(){
		@Override
		public GeneralItemVisibility toBean(JSONObject object) {
			GeneralItemVisibility gi = new GeneralItemVisibility();
			try {
				initBean(object, gi);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return gi;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			GeneralItemVisibility giv = (GeneralItemVisibility) genericBean;
			if (object.has("lastModificationDate")) giv.setLastModificationDate(object.getLong("lastModificationDate"));
			if (object.has("timeStamp")) giv.setTimeStamp(object.getLong("timeStamp"));
			if (object.has("status")) giv.setStatus(object.getInt("status"));
			if (object.has("email")) giv.setEmail(object.getString("email"));
			if (object.has("generalItemId")) giv.setGeneralItemId(object.getLong("generalItemId"));
		}

	};
    
}
