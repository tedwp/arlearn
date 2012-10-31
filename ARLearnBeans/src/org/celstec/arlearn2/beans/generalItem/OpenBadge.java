package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenBadge extends GeneralItem {
	
	private String badgeName;
	
	
	public String getBadgeName() {
		return badgeName;
	}


	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}



	@Override
	public boolean equals(Object obj) {
		OpenBadge other = (OpenBadge ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getBadgeName(), other.getBadgeName()); 

	}
	
	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			OpenBadge ou = (OpenBadge) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (ou.getBadgeName() != null) returnObject.put("badgeName", ou.getBadgeName());
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
			if (object.has("badgeName")) gi.setBadgeName(object.getString("badgeName"));
		}

	};
	
	public static void main(String[] args) throws JSONException {
		GamePackage gp = new GamePackage();
		OpenBadge ou = new OpenBadge();
		ou.setBadgeName("supporter badge");
		gp.addGeneralItem(ou);
		
		System.out.println(gp.toString());
		System.out.println(JsonBeanDeserializer.deserialize(gp.toString()).toString());
		
	}
}
