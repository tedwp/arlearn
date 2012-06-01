package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceTestSerializer extends GeneralItemSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		MultipleChoiceTest mct = (MultipleChoiceTest) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (mct.getText() != null) returnObject.put("text", mct.getText());
			if (mct.getRichText() != null) returnObject.put("richText", mct.getRichText());
			if (mct.getAnswers() != null) returnObject.put("answers", ListSerializer.toJSON(mct.getAnswers()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
}
