package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;

public class VideoObjectEditor extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected DynamicForm form;

	
	public VideoObjectEditor() {
		form = new DynamicForm();
		final TextItem videoText = new TextItem(VideoObject.VIDEO_FEED, constants.videoURL());
		videoText.setValidators(urlValidator);
		form.setFields(videoText);
		form.setWidth100();
		addMember(form);
	}
	
	public VideoObjectEditor(GeneralItem gi) {
		this();
		form.setValue(VideoObject.VIDEO_FEED, gi.getValueAsString(VideoObject.VIDEO_FEED));
	}
	
	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(VideoObject.VIDEO_FEED, form.getValueAsString(VideoObject.VIDEO_FEED));

	}
	
	public boolean validate() {
		return form.validate();
	}
	
	
	protected CustomValidator urlValidator = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			String stringValue = (""+value);
			if ((""+value).trim().equals("")) return false;
			if (!stringValue.startsWith("http://")) return false;
			return true;
		}
	};
}
