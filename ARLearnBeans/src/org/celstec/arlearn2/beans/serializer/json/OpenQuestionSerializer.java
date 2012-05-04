package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionSerializer implements JsonBean{

	@Override
	public JSONObject toJSON(Object bean) {
		OpenQuestion openQuestion = (OpenQuestion) bean;
		JSONObject returnObject = new JSONObject();
		try {
			returnObject.put("withAudio", openQuestion.isWithAudio());
			returnObject.put("withPicture", openQuestion.isWithPicture());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
