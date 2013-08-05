package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.NarratorItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;

import com.google.maps.gwt.client.LatLng;

public class NarratorItemWindow extends GeneralItemWindow {
	
	public NarratorItemWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle("Create Narrator Item");
		setWidth(500);
		setHeight(450);
	}

	@Override
	protected GeneralItem createItem() {
		NarratorItem ni = new NarratorItem();
		
		return ni;
	}

}
