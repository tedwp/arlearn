package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActionDeserializer extends RunBeanDeserializer{

	@Override
	public Action toBean(JSONObject object) {
		Action bean = new Action();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Action bean = (Action) genericBean;
		if (object.has("generalItemId")) bean.setGeneralItemId(object.getLong("generalItemId"));
		if (object.has("generalItemType")) bean.setGeneralItemType(object.getString("generalItemType"));
		if (object.has("userEmail")) bean.setUserEmail(object.getString("userEmail"));
		if (object.has("time")) bean.setTime(object.getLong("time"));
		if (object.has("action")) bean.setAction(object.getString("action"));

	}

}
