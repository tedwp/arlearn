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


/**
 * instances of ScoreDefinition define, how actions can be calculated into updated scores of a user.
 * Logic: when the action is submitted, we check:
 *  - if a score applies to this action
 *  - if the score has user scope: check, if this user had this event previously. If not, add the score.
 *  - if the score has team scope: check, if a team member had this event previously. If not, add the score.
 *  - if the score has all scope: check, if any user had this event previously. If not, add the score.
 *
 * @author klemke
 *
 */
public class ScoreDefinition extends GameBean {

	private String id;	
	private String action;
    private String generalItemId;
    private String generalItemType;
    
	private String scope;
	private Long value;

	public ScoreDefinition() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}

}
