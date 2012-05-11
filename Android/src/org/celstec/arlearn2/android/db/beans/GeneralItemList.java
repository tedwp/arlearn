package org.celstec.arlearn2.android.db.beans;

import java.util.ArrayList;
import java.util.List;

public class GeneralItemList extends RunBean {
	public static String generalItemsType = "org.celstec.arlearn2.android.db.beans.GeneralItem";

	private List<GeneralItem> generalItems = new ArrayList<GeneralItem>();

	public GeneralItemList() {

	}

	public List<GeneralItem> getGeneralitems() {
		return generalItems;
	}

	public void setGeneralItems(List<GeneralItem> giList) {
		this.generalItems = giList;
	}

	public void addGeneralItem(GeneralItem gi) {
		generalItems.add(gi);
	}

}
