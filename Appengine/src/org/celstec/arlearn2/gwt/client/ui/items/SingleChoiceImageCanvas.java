package org.celstec.arlearn2.gwt.client.ui.items;

import org.celstec.arlearn2.gwt.client.ui.items.MultiplechoiceCanvas.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class SingleChoiceImageCanvas extends MultiplechoiceCanvas{

	public SingleChoiceImageCanvas(String[] roles) {
		super(roles);
	}

	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest"));
		return result;
	}
	
	
	protected void generateAnswer(JSONArray ansArray, String textId, String cbId, int i) {
		if (form2.getValue(textId) != null) {
			JSONObject answer = new JSONObject();
			answer.put("imageUrl", new JSONString(form2.getValueAsString(textId)));
			if (form2.getValue("isNfcEnabled") != null && ((Boolean) form2.getValue("isNfcEnabled")))
				answer.put("nfcTag", new JSONString(form2.getValueAsString(cbId + "nfc")));
			answer.put("isCorrect", JSONBoolean.getInstance(form2.getValue(cbId) == null ? false : (Boolean) form2.getValue(cbId)));
			if (form2.getValue("answerid"+(i+1)) != null) {
				answer.put("id", new JSONString(form2.getValueAsString("answerid"+(i+1))));
			} else {
				answer.put("id",  new JSONString(UUID.uuid(15)));
			}
			ansArray.set(i, answer);
		}
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null)
			return;
		if (o.get("answers") != null) {
			JSONArray array = o.get("answers").isArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject ansObject = array.get(i).isObject();
				form2.setValue("answertext" + (i + 1), "Html");
				if (ansObject.get("isCorrect") != null)
					form2.setValue("answercb" + (i + 1), ansObject.get("isCorrect").isBoolean().booleanValue());
				if (ansObject.get("nfcTag") != null) {
					form2.setValue("isNfcEnabled", true);
					form2.setValue("answercb" + (i + 1) + "nfc", ansObject.get("nfcTag").isString().stringValue());
				}
				if (ansObject.get("id") != null) {
					form2.setValue("answerid"+ (i + 1), ansObject.get("id").isString().stringValue());
				}
				if (ansObject.get("imageUrl") != null)
					form2.setValue("answertext" + (i + 1), ansObject.get("imageUrl").isString().stringValue());
				// if (ansObject.get("id") != null);
			}
			form2.redraw();
		}

	}
}