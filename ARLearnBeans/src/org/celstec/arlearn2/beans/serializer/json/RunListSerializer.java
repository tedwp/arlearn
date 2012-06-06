package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.RunList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunListSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		RunList rl = (RunList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (rl.getRuns() != null) returnObject.put("runs", ListSerializer.toJSON(rl.getRuns()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
