package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseListDeserializer extends RunBeanDeserializer{

	@Override
	public ResponseList toBean(JSONObject object) {
		ResponseList al = new ResponseList();
		try {
			initBean(object, al);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		ResponseList actionList = (ResponseList) genericBean;
		if (object.has("responses")) actionList.setResponses(ListDeserializer.toBean(object.getJSONArray("responses"), Response.class));
	}


}
