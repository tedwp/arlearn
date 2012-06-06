package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.notification.RunModification;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunModificationSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		RunModification runBean = (RunModification) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getModificationType() != null) returnObject.put("modificationType", runBean.getModificationType());
			if (runBean.getRun() != null) returnObject.put("run", JsonBeanSerialiser.serialiseToJson(runBean.getRun()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
