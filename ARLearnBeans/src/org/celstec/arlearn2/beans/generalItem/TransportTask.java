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
