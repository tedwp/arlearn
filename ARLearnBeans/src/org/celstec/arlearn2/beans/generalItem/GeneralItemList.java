package org.celstec.arlearn2.beans.generalItem;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;

public class GeneralItemList extends Bean{

	public static String generalItemsType = "org.celstec.arlearn2.beans.generalItem.GeneralItem";

	private List<GeneralItem> generalItems = new ArrayList<GeneralItem>();

	public GeneralItemList() {

	}
	

	public List<GeneralItem> getGeneralItems() {
		return generalItems;
	}


	public void setGeneralItems(List<GeneralItem> giList) {
		this.generalItems = giList;
	}

	public void addGeneralItem(GeneralItem gi) {
		generalItems.add(gi);
	}
	
}
