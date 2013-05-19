package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;

import com.smartgwt.client.widgets.Canvas;

public class VideoObjectWindow extends GeneralItemWindow {
	
	VideoObjectEditor editor;
	
	public VideoObjectWindow(Game game) {
		super(game);
		setTitle("Create Video Object");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		VideoObject returnObject = new VideoObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new VideoObjectEditor();
		return editor;
	}
}
