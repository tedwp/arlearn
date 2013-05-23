package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class AudioExtensionEditor  extends VLayout implements ExtensionEditor{

	protected DynamicForm form;

	
	public AudioExtensionEditor() {
		form = new DynamicForm();
		final TextItem videoText = new TextItem(AudioObject.AUDIO_FEED, "Audio URL");
		form.setFields(videoText);
		form.setWidth100();
		addMember(form);
	}
	
	public AudioExtensionEditor(GeneralItem gi) {
		this();
		form.setValue(AudioObject.AUDIO_FEED, gi.getValueAsString(AudioObject.AUDIO_FEED));
	}

//	public void writeMetadataToObject(GeneralItem gi) {
//		gi.setString(VideoObject.VIDEO_FEED, form.getValueAsString(VideoObject.VIDEO_FEED));
//	}
	
	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(AudioObject.AUDIO_FEED, form.getValueAsString(AudioObject.AUDIO_FEED));

	}

	@Override
	public boolean validate() {
		return true;
	}
}