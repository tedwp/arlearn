package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor;

import com.smartgwt.client.widgets.Canvas;

public class SingleChoiceTestWindow extends GeneralItemWindow {
	
	SingleChoiceExtensionEditor editor;
	
	public SingleChoiceTestWindow(Game game) {
		super(game);
		setTitle("Create Single Choice Question");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		SingleChoiceTest returnObject = new SingleChoiceTest();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new SingleChoiceExtensionEditor();
		return editor;
	}
}