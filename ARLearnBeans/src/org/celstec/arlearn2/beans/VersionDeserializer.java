package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VersionDeserializer extends BeanDeserializer{
	
	@Override
	public Version toBean(JSONObject object) {
		Version returnObject = new Version();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Version version = (Version) genericBean;
		if (object.has("versionCode")) version.setVersionCode(object.getInt("versionCode"));
		if (object.has("serviceUrl")) version.setServiceUrl(object.getString("serviceUrl"));
	}

}
