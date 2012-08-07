package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.RunBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunBeanDeserializer extends BeanDeserializer{

	@Override
	public RunBean toBean(JSONObject object) {
		RunBean bean = new RunBean();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		RunBean bean = (RunBean) genericBean;
		if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));
		if (object.has("runId")) bean.setRunId(object.getLong("runId"));	
	}
}
