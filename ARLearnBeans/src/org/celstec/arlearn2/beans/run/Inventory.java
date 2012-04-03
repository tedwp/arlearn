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
