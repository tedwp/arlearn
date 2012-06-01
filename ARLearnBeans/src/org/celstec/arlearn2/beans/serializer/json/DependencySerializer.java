package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DependencySerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		JSONObject start = super.toJSON(bean);
		try {
			if (bean.getClass().equals(ActionDependency.class)) return toJsonAction(start, (ActionDependency) bean);
			if (bean.getClass().equals(TimeDependency.class)) return toJsonTime(start, (TimeDependency) bean);
			if (bean.getClass().equals(AndDependency.class)) return toJsonBoolean(start, (AndDependency) bean);
			if (bean.getClass().equals(OrDependency.class)) return toJsonBoolean(start, (OrDependency) bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject toJsonBoolean(JSONObject returnObject, BooleanDependency bean)  throws JSONException {
		if (bean.getType() != null) returnObject.put("type", bean.getType());
		if (bean.getDependencies()== null) return returnObject;
		JSONArray ar = new JSONArray();
		for (Dependency dep : bean.getDependencies()) {
			ar.put(JsonBeanSerialiser.serialiseToJson(dep));
		}
		returnObject.put("dependencies", ar);
		return returnObject;
	}

	private JSONObject toJsonAction(JSONObject returnObject, ActionDependency action) throws JSONException {
		if (action.getType() != null)
			returnObject.put("type", action.getType());
		if (action.getAction() != null)
			returnObject.put("action", action.getAction());
		if (action.getGeneralItemId() != null)
			returnObject.put("generalItemId", action.getGeneralItemId());
		if (action.getGeneralItemType() != null)
			returnObject.put("generalItemType", action.getGeneralItemType());

		return returnObject;
	}
	
	private JSONObject toJsonTime(JSONObject returnObject, TimeDependency bean)  throws JSONException {
		if (bean.getType() != null) returnObject.put("type", bean.getType());
		if (bean.getTimeDelta() != null) returnObject.put("timeDelta", bean.getTimeDelta());
		if (bean.getOffset() != null) returnObject.put("offset", JsonBeanSerialiser.serialiseToJson((bean.getOffset())));
		
		return returnObject;
	}

}
