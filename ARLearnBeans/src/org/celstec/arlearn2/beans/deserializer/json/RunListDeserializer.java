package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunListDeserializer extends BeanDeserializer{

	@Override
	public RunList toBean(JSONObject object) {
		RunList rl = new RunList();
		try {
			initBean(object, rl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rl;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		RunList runList = (RunList) genericBean;
		if (object.has("serverTime")) runList.setServerTime(object.getLong("serverTime"));
		if (object.has("runs")) runList.setRuns(ListDeserializer.toBean(object.getJSONArray("runs"), Run.class));
	}
}
