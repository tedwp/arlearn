package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;

import com.smartgwt.client.widgets.Canvas;

public class GeneralItemsManagement {

	public static String[] getItemTypes(boolean hidden) {
		String result[] = new String[3];
		result[0] = "Narrator Item";
		result[1] = VideoObject.HUMAN_READABLE_NAME;
		result[2] = YoutubeObject.HUMAN_READABLE_NAME;
		return result;
	}

	public static Canvas getMetadataExtensionEditor(GeneralItem gi) {
		if (gi.getType().equals(VideoObject.TYPE)) {
			return new VideoObjectEditor(gi);
		} else if (gi.getType().equals(YoutubeObject.TYPE)) {
			return new YoutubeObjectEditor(gi);
		}
		return new VideoObjectEditor();
	}
}
