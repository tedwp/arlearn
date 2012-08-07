package org.celstec.arlearn2.beans.dependencies;

import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;

public class ActionDependency extends Dependency {

	private String action;
	private Long generalItemId;
	private String generalItemType;
	private Integer scope;
	private String role;
	
	public ActionDependency() {
		setType(ActionDependency.class.getName());
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getGeneralItemId() {
		return generalItemId;
	}
	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}
	public String getGeneralItemType() {
		return generalItemType;
	}
	public void setGeneralItemType(String generalItemType) {
		this.generalItemType = generalItemType;
	}
	
	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public long satisfiedAt(List<Action> actionList) {
		long minSatisfiedAt = Long.MAX_VALUE;
		for (Iterator iterator = actionList.iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			boolean tempBool = true;
			if (!nullSafeEquals(action.getAction(), getAction())) tempBool = false;
			if (!nullSafeEquals(action.getGeneralItemType(), getGeneralItemType())) tempBool = false;
			if (!nullSafeEquals(action.getGeneralItemId(), getGeneralItemId())) tempBool = false;
			if (tempBool) minSatisfiedAt = Math.min(minSatisfiedAt, (action.getTime()==null)?0:action.getTime());
		}
		if (minSatisfiedAt == Long.MAX_VALUE) minSatisfiedAt = -1;
		return minSatisfiedAt;
	}
	

	@Override
	public boolean equals(Object obj) {
		ActionDependency other = (ActionDependency ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getAction(), other.getAction()) &&
			nullSafeEquals(getGeneralItemId(), other.getGeneralItemId()) &&
			nullSafeEquals(getScope(), other.getScope()) &&
			nullSafeEquals(getRole(), other.getRole()) &&
			nullSafeEquals(getGeneralItemType(), other.getGeneralItemType()) ; 

	}
	
}
