package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;

public class MultipleChoiceAnswer extends Bean {

	public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem";


	public MultipleChoiceAnswer() {
		super();
	}

	public MultipleChoiceAnswer(JSONObject object) {
		super(object);
	}

	public String getType() {
		return TYPE;
	}

}
