package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseSerializer extends RunBeanSerialiser {

	@Override
	public JSONObject toJSON(Object bean) {
		Response runBean = (Response) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getGeneralItemId() != null) returnObject.put("generalItemId", runBean.getGeneralItemId());
			if (runBean.getUserEmail() != null) returnObject.put("userEmail", runBean.getUserEmail());
			if (runBean.getResponseValue() != null) returnObject.put("responseValue", runBean.getResponseValue());
			if (runBean.getResponseItemId() != null) returnObject.put("responseItemId", runBean.getResponseItemId());
			if (runBean.getTimestamp() != null) returnObject.put("timestamp", runBean.getTimestamp());
			if (runBean.getRevoked() != null) returnObject.put("revoked", runBean.getRevoked());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
