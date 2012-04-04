package org.celstec.arlearn2.genericBeans;

import java.util.ArrayList;
import java.util.List;

public class GeneralItemList {

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
