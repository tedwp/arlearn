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
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenBadge extends GeneralItem {
	
	private String badgeUrl;
	private String image;
	private String evidence;
	
	
	public String getBadgeUrl() {
		return badgeUrl;
	}


	public void setBadgeUrl(String badgeurl) {
		this.badgeUrl = badgeurl;
	}



	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getEvidence() {
		return evidence;
	}


	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		OpenBadge other = (OpenBadge ) obj;
		return  
			nullSafeEquals(getBadgeUrl(), other.getBadgeUrl()); 

	}
	
	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			OpenBadge ou = (OpenBadge) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (ou.getBadgeUrl() != null) returnObject.put("badgeUrl", ou.getBadgeUrl());
				if (ou.getImage() != null) returnObject.put("image", ou.getImage());
				if (ou.getEvidence() != null) returnObject.put("evidence", ou.getEvidence());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){
		@Override
		public GeneralItem toBean(JSONObject object) {
			OpenBadge gi = new OpenBadge();
			try {
				initBean(object, gi);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return gi;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			OpenBadge gi = (OpenBadge) genericBean;
			if (object.has("badgeUrl")) gi.setBadgeUrl(object.getString("badgeUrl"));
			if (object.has("image")) gi.setImage(object.getString("image"));
			if (object.has("evidence")) gi.setEvidence(object.getString("evidence"));
		}

	};
	
//	public static void main(String[] args) throws JSONException {
//		GamePackage gp = new GamePackage();
//		OpenBadge ou = new OpenBadge();
//		ou.setBadgeUrl("supporter badge");
//		gp.addGeneralItem(ou);
//
//		System.out.println(gp.toString());
//		System.out.println(JsonBeanDeserializer.deserialize(gp.toString()).toString());
//
//	}
}
