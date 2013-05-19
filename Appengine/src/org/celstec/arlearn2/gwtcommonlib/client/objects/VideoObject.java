package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.VideoExtensionViewer;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer.YoutubeExtensionViewer;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;

public class VideoObject extends NarratorItem {

	public  final static String VIDEO_FEED = "videoFeed";
	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.VideoObject";
	public  final static String HUMAN_READABLE_NAME = "Video Object";
	public VideoObject() {
		super();
	}

	public VideoObject(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}
	
	public String getHumanReadableName() {
		return HUMAN_READABLE_NAME;
	}

	@Override
	public Canvas getViewerComponent() {
		VideoExtensionViewer ev = new VideoExtensionViewer();
		ev.loadGeneralItem(this);
		return ev;
	}
	
	public Canvas getMetadataExtensionEditor() {
		return new VideoObjectEditor(this);
	}
}
