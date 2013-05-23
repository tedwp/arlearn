package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.YoutubeExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class NarratorItem extends GeneralItem {
	
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.NarratorItem";

	public NarratorItem() {
		super();
	}

	public NarratorItem(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}
	
	@Override
	public Canvas getViewerComponent() {
		return new Canvas();
	}
	
	public Canvas getMetadataExtensionEditor() {
		return null;
	}

	@Override
	public boolean enableDataCollection() {
		return true;
	}
	
}
