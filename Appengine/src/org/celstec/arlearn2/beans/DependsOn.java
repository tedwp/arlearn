package org.celstec.arlearn2.beans;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DependsOn {
	
	public static final int TEAM_SCOPE = 0;
	public static final int USER_SCOPE = 1;
	public static final int ALL_SCOPE = 2;
	
	
	private String action;
	private String generalItemId;
	private String generalItemType;
	
	
	private int scope;
	
	public DependsOn(){
		
	}
	
	public DependsOn(String jsonString){
		try {
			JSONObject object = new JSONObject(jsonString);
			if (object.has("action")) setAction(object.getString("action"));
			if (object.has("generalItemId")) setGeneralItemId(object.getString("generalItemId"));
			if (object.has("generalItemType")) setGeneralItemType(object.getString("generalItemType"));
			if (object.has("scope")) {
				if (object.getString("scope").equalsIgnoreCase("user")){
					setScope(USER_SCOPE);
				} else if (object.getString("scope").equalsIgnoreCase("team")){
					setScope(TEAM_SCOPE);
				} else if (object.getString("scope").equalsIgnoreCase("all")){
					setScope(ALL_SCOPE);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(String generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getGeneralItemType() {
		return generalItemType;
	}

	public void setGeneralItemType(String generalItemType) {
		this.generalItemType = generalItemType;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public boolean checkActions(ActionList al) {
		Iterator<Action>it = al.getActions().iterator();
		while (it.hasNext()) {
			if (checkAction((Action) it.next())) return true;
		}
		return false;
	}

	public boolean checkAction(Action a) {
		if (getAction() != null && !getAction().equals(a.getAction())) return false;
		if (getGeneralItemId() != null && !getGeneralItemId().equals(a.getGeneralItemId())) return false;
		if (getGeneralItemType() != null && !getGeneralItemType().equals(a.getGeneralItemType())) return false;
		return true;
	}
	

}
