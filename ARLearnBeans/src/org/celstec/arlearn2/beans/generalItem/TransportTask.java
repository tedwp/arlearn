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
package org.celstec.arlearn2.beans.generalItem;

public class TransportTask extends GeneralItem {

	private String pickupItemId;
	
	private String dropZoneId;
	
	public TransportTask() {
		super();
	}

	public String getPickupItemId() {
		return pickupItemId;
	}

	public void setPickupItemId(String pickupItemId) {
		this.pickupItemId = pickupItemId;
	}

	public String getDropZoneId() {
		return dropZoneId;
	}

	public void setDropZoneId(String dropZoneId) {
		this.dropZoneId = dropZoneId;
	}
	
	
}
