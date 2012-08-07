package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.RunBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunBeanSerialiser extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		RunBean runBean = (RunBean) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getRunId() != null) returnObject.put("runId", runBean.getRunId());
			if (runBean.getDeleted() != null) returnObject.put("deleted", runBean.getDeleted());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
