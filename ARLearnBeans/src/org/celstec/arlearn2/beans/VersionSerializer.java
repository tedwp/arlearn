package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VersionSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		Version versionBean = (Version) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (versionBean.getVersionCode() != null) returnObject.put("versionCode", versionBean.getVersionCode());
			if (versionBean.getServiceUrl() != null) returnObject.put("serviceUrl", versionBean.getServiceUrl());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
