package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.AudioExtensionEditor;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.Canvas;

public class AudioObjectWindow extends GeneralItemWindow {
	
	AudioExtensionEditor editor;
	
	public AudioObjectWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		super(generalItemsTab, coordinate);
		setTitle("Create Audio Object");
		setWidth(500);
		setHeight(450);
	}
	
	@Override
	protected GeneralItem createItem() {
		AudioObject returnObject = new AudioObject();
		editor.saveToBean(returnObject);
		return returnObject;
	}
	
	protected Canvas getMetadataExtensions() {
		editor = new AudioExtensionEditor();
		return editor;
	}
	
	public boolean validate() {
		return super.validate() && editor.validate();
	}
}
