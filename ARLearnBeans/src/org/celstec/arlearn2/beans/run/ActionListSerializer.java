package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActionListSerializer  extends RunBeanSerialiser{

	@Override
	public JSONObject toJSON(Object bean) {
		ActionList al = (ActionList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (al.getActions() != null) returnObject.put("actions", ListSerializer.toJSON(al.getActions()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
