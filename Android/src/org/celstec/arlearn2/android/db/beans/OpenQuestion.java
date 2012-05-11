package org.celstec.arlearn2.android.db.beans;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Deprecated
public class OpenQuestion  extends NarratorItem {
	
	public static final String TYPE = "OpenQuestion";

	private String question;
	
	public OpenQuestion(GeneralItem gi) {
		super(gi);
		if (gi.getPayload() != null) {
			try {
				JSONObject object = new JSONObject(gi.getPayload());
				if (object != null) {
					if (object.has("question"))
						setQuestion(object.getString("question"));
				}
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}

	public OpenQuestion() {
		super();
	}

	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public JSONObject getSpecificPartAsJson() {
		JSONObject json =super.getSpecificPartAsJson();
		try {
			if (getQuestion()!=null) json.put("question", getQuestion());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
