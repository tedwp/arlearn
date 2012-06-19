package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActionListDeserializer extends RunBeanDeserializer{

	@Override
	public ActionList toBean(JSONObject object) {
		ActionList al = new ActionList();
		try {
			initBean(object, al);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		ActionList actionList = (ActionList) genericBean;
		if (object.has("actions")) actionList.setActions(ListDeserializer.toBean(object.getJSONArray("actions"), Action.class));
	}

}
