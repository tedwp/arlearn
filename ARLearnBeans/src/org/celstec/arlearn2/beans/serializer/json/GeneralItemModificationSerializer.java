package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemModificationSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		GeneralItemModification runBean = (GeneralItemModification) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getModificationType() != null) returnObject.put("modificationType", runBean.getModificationType());
			if (runBean.getRunId() != null) returnObject.put("runId", runBean.getRunId());
			if (runBean.getGeneralItem() != null) returnObject.put("generalItem", JsonBeanSerialiser.serialiseToJson(runBean.getGeneralItem()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}

