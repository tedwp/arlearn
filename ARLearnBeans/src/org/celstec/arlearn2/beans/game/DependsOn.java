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
package org.celstec.arlearn2.beans.game;

import java.io.Serializable;

@Deprecated
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
