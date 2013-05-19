package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class YoutubeObjectEditor extends VLayout implements ExtensionEditor{

	protected DynamicForm form;

	
	public YoutubeObjectEditor() {
		form = new DynamicForm();
		final TextItem videoText = new TextItem(YoutubeObject.YOUTUBE_URL, "Youtube URL");
		form.setFields(videoText);
		form.setWidth100();
		addMember(form);
	}
	
	public YoutubeObjectEditor(GeneralItem gi) {
		this();
		form.setValue(YoutubeObject.YOUTUBE_URL, gi.getValueAsString(YoutubeObject.YOUTUBE_URL));
	}

//	public void writeMetadataToObject(GeneralItem gi) {
//		gi.setString(YoutubeObject.YOUTUBE_URL, form.getValueAsString(YoutubeObject.YOUTUBE_URL));
//	}

	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(YoutubeObject.YOUTUBE_URL, form.getValueAsString(YoutubeObject.YOUTUBE_URL));
		
	}
}
