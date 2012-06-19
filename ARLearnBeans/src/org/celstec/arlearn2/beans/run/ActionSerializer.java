package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActionSerializer extends RunBeanSerialiser {

	@Override
	public JSONObject toJSON(Object bean) {
		Action runBean = (Action) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getGeneralItemId() != null) returnObject.put("generalItemId", runBean.getGeneralItemId());
			if (runBean.getGeneralItemType() != null) returnObject.put("generalItemType", runBean.getGeneralItemType());
			if (runBean.getUserEmail() != null) returnObject.put("userEmail", runBean.getUserEmail());
			if (runBean.getTime() != null) returnObject.put("time", runBean.getTime());
			if (runBean.getAction() != null) returnObject.put("action", runBean.getAction());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
