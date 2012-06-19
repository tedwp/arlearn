package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItemDeserializer;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceTestDeserializer extends GeneralItemDeserializer{

	@Override
	public MultipleChoiceTest toBean(JSONObject object) {
		MultipleChoiceTest mct = new MultipleChoiceTest();
		try {
			initBean(object, mct);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mct;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		MultipleChoiceTest mctItem = (MultipleChoiceTest) genericBean;
		if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
		if (object.has("text")) mctItem.setText(object.getString("text"));
		if (object.has("answers")) mctItem.setAnswers(ListDeserializer.toBean(object.getJSONArray("answers"), MultipleChoiceAnswerItem.class));
	}
}
