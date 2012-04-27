
package org.celstec.arlearn2.beans.game;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class Config extends Bean {

	
	private Boolean scoring;
	private Boolean mapAvailable;
	public static String manualItemsType = "org.celstec.arlearn2.beans.generalItem.GeneralItem";
	private List<GeneralItem> manualItems = new ArrayList<GeneralItem>();

	public Boolean getScoring() {
		return scoring;
	}

	public void setScoring(Boolean scoring) {
		this.scoring = scoring;
	}

	public Boolean getMapAvailable() {
		return mapAvailable;
	}

	public void setMapAvailable(Boolean mapAvailable) {
		this.mapAvailable = mapAvailable;
	}

	public List<GeneralItem> getManualItems() {
		return manualItems;
	}

	public void setManualItems(List<GeneralItem> manualItems) {
		this.manualItems = manualItems;
	}
	
}
