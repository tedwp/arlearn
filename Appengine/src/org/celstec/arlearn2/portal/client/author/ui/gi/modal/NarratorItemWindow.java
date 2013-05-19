package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.NarratorItem;

public class NarratorItemWindow extends GeneralItemWindow {
	
	public NarratorItemWindow(Game game) {
		super(game);
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
