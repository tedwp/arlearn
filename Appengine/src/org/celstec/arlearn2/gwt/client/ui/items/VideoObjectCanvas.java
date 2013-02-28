/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwt.client.ui.items;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class VideoObjectCanvas extends NarratorItemCanvas {

	protected TextItem videoFeed;

	private final String VIDEOFEED = "videoFeed";
	
	public VideoObjectCanvas(String roles[]) {
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
		form1.setFields(idItem, gameIdItem, nameItem, videoFeed);
		addField(form1,idItem, gameIdItem, nameItem, videoFeed);
	}
	
	protected void createVideoFeedItem() {
		videoFeed = new TextItem(VIDEOFEED);
		videoFeed.setTitle(constants.videoUrl());
		videoFeed.setWrapTitle(false);
		videoFeed.setValidators(urlValidator);
	}
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.VideoObject"));
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
