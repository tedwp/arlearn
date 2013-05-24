package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;

import com.smartgwt.client.widgets.Canvas;

public class YoutubeObjectWindow extends GeneralItemWindow {
	
	YoutubeObjectEditor editor;
	
	public YoutubeObjectWindow(Game game) {
		super(game);
		setTitle("Create Youtube Object");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		YoutubeObject returnObject = new YoutubeObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new YoutubeObjectEditor();
		return editor;
	}
}