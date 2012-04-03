package org.celstec.arlearn2.beans.dependencies;

import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

public class ActionDependency extends Dependency {

	private String action;
	private Long generalItemId;
	private String generalItemType;
	
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
	
	private boolean nullSafeEquals(Object act1, Object act2) {
		if (act2 == null || act1 == act2) return true;
		if (act1 == null) return false;
		return act1.equals(act2);
	}
	
	
}
