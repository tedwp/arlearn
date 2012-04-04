package org.celstec.arlearn2.beans;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class FindLocationQuestion extends OpenQuestion {

	private String destinationId;
	
	public FindLocationQuestion() {
		super();
	}

	public FindLocationQuestion(String payload) {
		super(payload);
		if (payload !=null) {
			try {
				JSONObject object = new JSONObject(payload);
				if (object.has("destinationId")) setDestinationId(object.getString("destinationId"));
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

}
