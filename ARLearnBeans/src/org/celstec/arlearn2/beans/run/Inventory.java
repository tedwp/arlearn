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
package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;;

public class Inventory extends RunBean {

	private GeneralItemList mapInventoryItems;
	private GeneralItemList userInventoryItems;
	private GeneralItemList teamInventoryItems;

	public Inventory() {
	}
	
	public GeneralItemList getMapInventoryItems() {
		return mapInventoryItems;
	}
	
	public void setMapInventoryItems(GeneralItemList mapInventoryItems) {
		this.mapInventoryItems = mapInventoryItems;
	}
	
	public GeneralItemList getUserInventoryItems() {
		return userInventoryItems;
	}
	
	public void setUserInventoryItems(GeneralItemList userInventoryItems) {
		this.userInventoryItems = userInventoryItems;
	}
	
	public GeneralItemList getTeamInventoryItems() {
		return teamInventoryItems;
	}
	
	public void setTeamInventoryItems(GeneralItemList teamInventoryItems) {
		this.teamInventoryItems = teamInventoryItems;
	}
}
