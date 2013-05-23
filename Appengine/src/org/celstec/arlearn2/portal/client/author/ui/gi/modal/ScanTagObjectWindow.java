package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ScanTagEditor;

import com.smartgwt.client.widgets.Canvas;

public class ScanTagObjectWindow  extends GeneralItemWindow {
	
	ScanTagEditor editor;
	
	public ScanTagObjectWindow(Game game) {
		super(game);
		setTitle("Create Youtube Object");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		ScanTagObject returnObject = new ScanTagObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new ScanTagEditor();
		return editor;
	}
}
