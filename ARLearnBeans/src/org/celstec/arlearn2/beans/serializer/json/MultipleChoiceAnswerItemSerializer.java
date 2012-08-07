package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceAnswerItemSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		MultipleChoiceAnswerItem mcai = (MultipleChoiceAnswerItem) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (mcai.getAnswer() != null) returnObject.put("answer", mcai.getAnswer());
			if (mcai.getId() != null) returnObject.put("id", mcai.getId());
			if (mcai.getNfcTag() != null) returnObject.put("nfcTag", mcai.getNfcTag());
			if (mcai.getIsCorrect() != null) returnObject.put("isCorrect", mcai.getIsCorrect());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
