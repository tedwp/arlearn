package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceAnswerItemDeserializer extends BeanDeserializer{

	@Override
	public MultipleChoiceAnswerItem toBean(JSONObject object) {
		MultipleChoiceAnswerItem mcai = new MultipleChoiceAnswerItem();
		try {
			initBean(object, mcai);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mcai;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		MultipleChoiceAnswerItem mcai = (MultipleChoiceAnswerItem) genericBean;
		if (object.has("isCorrect")) mcai.setIsCorrect(object.getBoolean("isCorrect"));
		if (object.has("answer")) mcai.setAnswer(object.getString("answer"));
		if (object.has("nfcTag")) mcai.setNfcTag(object.getString("nfcTag"));
		if (object.has("id")) mcai.setId(object.getString("id"));
	}
}
