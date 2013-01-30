package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SingleChoiceImageTest extends SingleChoiceTest {

	private String audioQuestion;
	
	public String getAudioQuestion() {
		return audioQuestion;
	}

	public void setAudioQuestion(String audioQuestion) {
		this.audioQuestion = audioQuestion;
	}

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
			if (object.has("audioQuestion")) mctItem.setAudioQuestion(object.getString("audioQuestion"));
			if (object.has("answers")) mctItem.setAnswers(ListDeserializer.toBean(object.getJSONArray("answers"), MultipleChoiceImageAnswerItem.class));
		}
	};

	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			SingleChoiceImageTest mct = (SingleChoiceImageTest) bean;
			JSONObject returnObject = SingleChoiceTest.serializer.toJSON(bean);
			try {
				if (mct.getAudioQuestion() != null) returnObject.put("audioQuestion", mct.getAudioQuestion());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
		
	};
}
