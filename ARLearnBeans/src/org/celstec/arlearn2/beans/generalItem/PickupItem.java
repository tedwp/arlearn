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
