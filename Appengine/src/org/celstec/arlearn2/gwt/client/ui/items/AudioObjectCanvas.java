package org.celstec.arlearn2.gwt.client.ui.items;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class AudioObjectCanvas extends NarratorItemCanvas {

	protected TextItem audioFeed;

	private final String AUDIOFEED = "audioFeed";
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	
	public AudioObjectCanvas(String roles[]) {
		super(roles);
	}
	@Override
	public boolean validateForm() {
		return false;
	}
	
	protected void initComponents() {
		super.initComponents();
		createAudioFeedComponent();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, audioFeed);
		addField(form1,idItem, gameIdItem, nameItem, audioFeed);
	}
	
	protected void createAudioFeedComponent() {
		audioFeed = new TextItem(AUDIOFEED);
		audioFeed.setTitle(constants.audioUrl());
		audioFeed.setWrapTitle(false);
	}
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		putStringValue(result, AUDIOFEED);

		return result;
	}

	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		setValueString(AUDIOFEED, o);
		form1.redraw();
		form2.redraw();
	}
}
