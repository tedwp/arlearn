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

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemVisibilityList extends Bean{

	public static String GeneralItemVisibilityType = "org.celstec.arlearn2.beans.run.GeneralItemVisibility";
	
	private List<GeneralItemVisibility> generalItemsVisibility = new ArrayList<GeneralItemVisibility>();

	public GeneralItemVisibilityList() {

	}
	
	private Long serverTime;

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	

	public List<GeneralItemVisibility> getGeneralItemsVisibility() {
		return generalItemsVisibility;
	}

	public void setGeneralItemsVisibility(List<GeneralItemVisibility> generalItemsVisibility) {
		this.generalItemsVisibility = generalItemsVisibility;
	}

	public void addGeneralItemVisibility(GeneralItemVisibility gi) {
		generalItemsVisibility.add(gi);
	}
	
	public static BeanSerializer serializer = new BeanSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			GeneralItemVisibilityList tl = (GeneralItemVisibilityList) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (tl.getServerTime() != null) returnObject.put("serverTime", tl.getServerTime());
				if (tl.getGeneralItemsVisibility() != null) returnObject.put("generalItemsVisibility", ListSerializer.toJSON(tl.getGeneralItemsVisibility()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static BeanDeserializer deserializer = new BeanDeserializer() {

		@Override
		public GeneralItemVisibilityList toBean(JSONObject object) {
			GeneralItemVisibilityList tl = new GeneralItemVisibilityList();
			try {
				initBean(object, tl);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return tl;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			GeneralItemVisibilityList giList = (GeneralItemVisibilityList) genericBean;
			if (object.has("serverTime")) giList.setServerTime(object.getLong("serverTime"));
			if (object.has("generalItemsVisibility")) giList.setGeneralItemsVisibility(ListDeserializer.toBean(object.getJSONArray("generalItemsVisibility"), GeneralItemVisibilityList.class));
		}
	};
	
}
