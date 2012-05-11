package org.celstec.arlearn2.android.db.beans;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceAnswerItem {
	private Boolean isCorrect;
	private String answer;
	private String id;

	public MultipleChoiceAnswerItem() {

	}
	
	public MultipleChoiceAnswerItem(JSONObject object) {
		try {
			if (object != null) {
				if (object.has("id")) setId(object.getString("id"));
				if (object.has("answer")) setAnswer(object.getString("answer"));
				if (object.has("isCorrect")) setIsCorrect(object.getBoolean("isCorrect"));
			}
		} catch (JSONException e) {
			// in case of a JSON exception, the fields are not filled in
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getSpecificPartAsJson() {
		try {
			JSONObject json = new JSONObject();
			if (getIsCorrect()!=null) json.put("isCorrect", getIsCorrect());
			if (getAnswer()!=null) json.put("answer", getAnswer());
			if (getId()!=null) json.put("id", getId());
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
