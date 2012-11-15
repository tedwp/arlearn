package org.celstec.arlearn2.gwt.client.ui.items;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class SingleChoiceCanvas extends MultiplechoiceCanvas{

	public SingleChoiceCanvas(String[] roles) {
		super(roles);
	}

	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.SingleChoiceTest"));
		return result;
	}
}
