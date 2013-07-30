/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.beans.dependencies;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	public long satisfiedAt(List<Action> actionList, Map<String, String[]> accountRoleMap) {
		long minSatisfiedAt = Long.MAX_VALUE;
		for (Iterator iterator = actionList.iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			boolean tempBool = true;
			if (!nullSafeEquals(action.getAction(), getAction())) tempBool = false;
			if (!nullSafeEquals(action.getGeneralItemType(), getGeneralItemType())) tempBool = false;
			if (!nullSafeEquals(action.getGeneralItemId(), getGeneralItemId())) tempBool = false;
			if (getRole() != null && action.getUserEmail() != null) {
				String[] roles = accountRoleMap.get(action.getUserEmail());
				if (roles == null) {
					tempBool = false;
				} else {
					if (!containsRole(roles, action.getUserEmail())) tempBool = false;
				}
				
			}
			if (tempBool) minSatisfiedAt = Math.min(minSatisfiedAt, (action.getTime()==null)?0:action.getTime());
		}
		if (minSatisfiedAt == Long.MAX_VALUE) minSatisfiedAt = -1;
		return minSatisfiedAt;
	}
	
	private boolean containsRole(String roles[], String userRole) {
		for (String role: roles) {
			if (role.equals(userRole)) return true;
		}
		return false;
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

    /**
     * hashCode is necessary to be able to use ActionDependencies as keys in HashMaps.
     *
     * @return
     */
    public int hashCode() {
        int hash = 0;
        if (getAction() != null) hash += getAction().hashCode();
        if (getGeneralItemId() != null) hash += getGeneralItemId().hashCode();
        if (getScope() != null) hash += getScope().hashCode();
        if (getRole() != null) hash += getRole().hashCode();
        if (getGeneralItemType() != null) hash += getGeneralItemType().hashCode();
        return hash;
    }
	
}
