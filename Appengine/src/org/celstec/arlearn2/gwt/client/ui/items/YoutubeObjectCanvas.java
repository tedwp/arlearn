package org.celstec.arlearn2.gwt.client.ui.items;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class YoutubeObjectCanvas extends NarratorItemCanvas {

	protected TextItem youtubeFeed;

	private final String VIDEOFEED = "youtubeUrl";
	
	public YoutubeObjectCanvas(String roles[]) {
		super(roles);
	}
	@Override
	public boolean validateForm() {
		return super.validateForm();
	}
	
	protected void initComponents() {
		super.initComponents();
		createVideoFeedItem();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, youtubeFeed);
		addField(form1,idItem, gameIdItem, nameItem, youtubeFeed);
	}
	
	protected void createVideoFeedItem() {
		youtubeFeed = new TextItem(VIDEOFEED);
		youtubeFeed.setTitle("Youtube url"); //TODO youtube constant
		youtubeFeed.setWrapTitle(false);
		youtubeFeed.setValidators(urlValidator);
	}
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.YoutubeObject"));
		putStringValue(result, VIDEOFEED);

		return result;
	}

	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		setValueString(VIDEOFEED, o);
		form1.redraw();
		form2.redraw();
	}
	
	
}

