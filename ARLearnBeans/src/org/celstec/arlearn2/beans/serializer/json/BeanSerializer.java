package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BeanSerializer implements JsonBean {

	@Override
	public JSONObject toJSON(Object bean) {
		Bean beanBean = (Bean) bean;
		JSONObject returnObject = new JSONObject();
		try {
			if (beanBean.getType() != null) returnObject.put("type", beanBean.getType());			
			if (beanBean.getError() != null) returnObject.put("error", beanBean.getError());
			if (beanBean.getErrorCode() != null) returnObject.put("errorCode", beanBean.getErrorCode());
			if (beanBean.getTimestamp() != null) returnObject.put("timestamp", beanBean.getTimestamp());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
