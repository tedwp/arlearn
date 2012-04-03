package org.celstec.arlearn2.beans.game;

import java.io.Serializable;

public class DependsOn implements Serializable {
	
	public static final int TEAM_SCOPE = 0;
	public static final int USER_SCOPE = 1;
	public static final int ALL_SCOPE = 2;
	
	
	private String action;
	private String generalItemId;
	private String generalItemType;
	
	
	private Integer scope;
	
	public DependsOn(){
		
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
		if (scope == null) return -1;
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}
	
	
}
