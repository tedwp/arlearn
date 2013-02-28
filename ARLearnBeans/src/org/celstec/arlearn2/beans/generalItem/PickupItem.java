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

/**
 * The PickupItem is a locateable object on a map that can be picked up by a user
 * The PickupItem may be associated with a DropZone, which denotes a destination.
 * 
 * @author klemke
 *
 */
public class PickupItem extends NarratorItem {

	private String dropZoneId;
	
	//TODO define itemType
	
	public PickupItem() {
		super();
	}

	public String getDropZoneId() {
		return dropZoneId;
	}

	public void setDropZoneId(String dropZoneId) {
		this.dropZoneId = dropZoneId;
	}
	
}
