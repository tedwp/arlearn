package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		OpenQuestion openQuestion = (OpenQuestion) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			returnObject.put("withAudio", openQuestion.isWithAudio());
			returnObject.put("withText", openQuestion.isWithText());
			returnObject.put("withPicture", openQuestion.isWithPicture());
			returnObject.put("withVideo", openQuestion.isWithVideo());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
