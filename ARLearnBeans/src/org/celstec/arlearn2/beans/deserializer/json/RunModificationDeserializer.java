package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunModificationDeserializer extends BeanDeserializer{

	@Override
	public RunModification toBean(JSONObject object) {
		RunModification bean = new RunModification();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		RunModification bean = (RunModification) genericBean;
		if (object.has("modificationType")) bean.setModificationType(object.getInt("modificationType"));
		if (object.has("run")) bean.setRun((Run) JsonBeanDeserializer.deserialize(Run.class, object.getJSONObject("run")));

	}
}

