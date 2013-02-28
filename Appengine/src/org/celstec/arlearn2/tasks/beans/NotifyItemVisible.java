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
package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.notification.GeneralitemNotification;

public class NotifyItemVisible extends GenericBean{
	
	private Long runId;
	private Long generalItemId;
	private String name;
	private String scope;
	private Double lat;
	private Double lng;
	
	
	public NotifyItemVisible() {
		
	}
	
	public NotifyItemVisible(String token, Long runId, GeneralItem gi) {
		super(token);
		this.runId = runId;
		generalItemId = gi.getId();
		name = gi.getName();
		lat = gi.getLat();
		lng = gi.getLng();
		scope = gi.getScope();
	}
	

	public NotifyItemVisible(String token, Long runId, Long generalItemId, String name, String scope, Double lat, Double lng) {
		super(token);
		this.runId = runId;
		this.generalItemId = generalItemId;
		this.name = name;
		this.scope = scope;
		this.lat = lat;
		this.lng = lng;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public void run() {
		GeneralitemNotification gin = new GeneralitemNotification(getScope(), getRunId(), getToken());
		gin.itemVisible(this);
		
	}
	
	

}
