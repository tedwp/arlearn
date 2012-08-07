package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DependencyDeserializer extends BeanDeserializer {

	private final String ACTION_DEP = "org.celstec.arlearn2.beans.dependencies.ActionDependency";
	private final String AND_DEP = "org.celstec.arlearn2.beans.dependencies.AndDependency";
	private final String OR_DEP = "org.celstec.arlearn2.beans.dependencies.OrDependency";
	private final String TIME_DEP = "org.celstec.arlearn2.beans.dependencies.TimeDependency";
	
	@Override
	public Dependency toBean(JSONObject object) {
		try {
			if (!object.has("type")) return null;
			return initBean(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Dependency initBean(JSONObject object) throws JSONException {
		Dependency dep = null;
		String type = object.getString("type");
		if (type != null) {
		if (ACTION_DEP.equals(type)) dep = toBeanAction(object, type);
		if (AND_DEP.equals(type)) dep = toAndAction(object, type);
		if (OR_DEP.equals(type)) dep = toOrAction(object, type);
		if (TIME_DEP.equals(type)) dep = toTimeAction(object, type);
		
		super.initBean(object, dep);
		}
		return dep;
		
	}
	
	private Dependency toTimeAction(JSONObject object, String type) throws JSONException {
		TimeDependency dep = new TimeDependency();
		if (object.has("timeDelta")) dep.setTimeDelta(object.getLong("timeDelta"));
		if (object.has("offset")) dep.setOffset((Dependency) JsonBeanDeserializer.deserialize(Dependency.class, object.getJSONObject("offset")));
		return dep;
	}

	private Dependency toOrAction(JSONObject object, String type) throws JSONException {
		OrDependency dep = new OrDependency();
		if (object.has("dependencies")) {
			JSONArray deps = object.getJSONArray("dependencies");
			for (int i =0; i<deps.length();i++) {
				dep.addDependecy((Dependency) JsonBeanDeserializer.deserialize(Dependency.class,deps.getJSONObject(i)));
			}
		}
		return dep;
	}

	private Dependency toAndAction(JSONObject object, String type) throws JSONException {
		AndDependency dep = new AndDependency();
		if (object.has("dependencies")) {
			JSONArray deps = object.getJSONArray("dependencies");
			for (int i =0; i<deps.length();i++) {
				dep.addDependecy((Dependency) JsonBeanDeserializer.deserialize(Dependency.class,deps.getJSONObject(i)));
			}
		}
		return dep;
	}

	private ActionDependency toBeanAction(JSONObject object, String type) throws JSONException {
		ActionDependency dep = new ActionDependency();
		if (object.has("action")) dep.setAction(object.getString("action"));
		if (object.has("generalItemId")) dep.setGeneralItemId(object.getLong("generalItemId"));
		if (object.has("generalItemType")) dep.setGeneralItemType(object.getString("generalItemType"));
		if (object.has("scope")) dep.setScope(object.getInt("scope"));
		if (object.has("role")) dep.setRole(object.getString("role"));
		return dep;
	}

}
