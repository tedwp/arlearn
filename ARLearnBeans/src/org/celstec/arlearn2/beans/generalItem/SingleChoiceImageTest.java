package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SingleChoiceImageTest extends SingleChoiceTest {

	
	public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

		@Override
		public SingleChoiceImageTest toBean(JSONObject object) {
			SingleChoiceImageTest mct = new SingleChoiceImageTest();
			try {
				initBean(object, mct);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return mct;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			SingleChoiceImageTest mctItem = (SingleChoiceImageTest) genericBean;
			if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
			if (object.has("text")) mctItem.setText(object.getString("text"));
			if (object.has("answers")) mctItem.setAnswers(ListDeserializer.toBean(object.getJSONArray("answers"), MultipleChoiceImageAnswerItem.class));
		}
	}
;
}
