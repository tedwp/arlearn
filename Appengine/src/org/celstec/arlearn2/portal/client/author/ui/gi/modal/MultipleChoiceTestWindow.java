package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceExtensionEditor;

import com.smartgwt.client.widgets.Canvas;

public class MultipleChoiceTestWindow extends GeneralItemWindow {
	
	MultipleChoiceExtensionEditor editor;
	
	public MultipleChoiceTestWindow(Game game) {
		super(game);
		setTitle("Create Multiple Choice Question");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		MultipleChoiceTest returnObject = new MultipleChoiceTest();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new MultipleChoiceExtensionEditor();
		return editor;
	}
}
