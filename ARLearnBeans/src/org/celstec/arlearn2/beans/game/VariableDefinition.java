package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VariableDefinition extends Bean{
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public VariableDefinition toBean(JSONObject object) {
			VariableDefinition bean = new VariableDefinition();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			VariableDefinition bean = (VariableDefinition) genericBean;
			if (object.has("name")) bean.setName(object.getString("name"));
			
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			VariableDefinition teamBean = (VariableDefinition) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (teamBean.getName() != null) returnObject.put("name", teamBean.getName());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
