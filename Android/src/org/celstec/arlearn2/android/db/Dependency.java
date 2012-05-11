package org.celstec.arlearn2.android.db;


import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Dependency {

	private boolean hasDeps = true;
	private String action;
	private String generalItemId;
	
	@Deprecated
	public Dependency(String jsonString) {
		if (jsonString == null) {
			hasDeps = false;
		} else {
		JSONObject dependsOn;
		try {
			dependsOn = new JSONObject(jsonString);
			if (dependsOn.has("action")) action = dependsOn.getString("action");
			if (dependsOn.has("generalItemId")) generalItemId = dependsOn.getString("generalItemId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
	}
	
	public Dependency(DependsOn dependsOn) {
		if (dependsOn.getAction() == null && dependsOn.getGeneralItemId()==null && dependsOn.getGeneralItemType() == null) hasDeps = false;
		if (dependsOn.getAction() != null) action = dependsOn.getAction();
		if (dependsOn.getGeneralItemId() != null) generalItemId = dependsOn.getGeneralItemId();
	}

	public boolean allDepenciesAreMet(Action[] allActions) {
		if (!hasDeps) return true;
		for (int i = 0; i < allActions.length; i++) {
			boolean result = true;
			if (action != null && !action.equals(allActions[i].getAction())) result = false;
			if (generalItemId != null && !generalItemId.equals(allActions[i].getGeneralItemId())) result = false;
			if (result) return true;
		}
		return false;
	}

}
