package org.celstec.arlearn2.beans.generalItem;

import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.NarratorItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.celstec.arlearn2.beans.serializer.json.NarratorItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SingleChoiceTest extends GeneralItem {

	public static String answersType = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem";

	private List<MultipleChoiceAnswerItem> answers = new Vector();
	private String richText;
	private String text;
	
	public SingleChoiceTest() {
		
	}
	
	public List<MultipleChoiceAnswerItem> getAnswers() {
		return answers;
	}
	public void setAnswers(List<MultipleChoiceAnswerItem> answers) {
		this.answers = answers;
	}
	public String getRichText() {
		return richText;
	}
	public void setRichText(String richText) {
		this.richText = richText;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		MultipleChoiceTest other = (MultipleChoiceTest ) obj;
		return 
			nullSafeEquals(getAnswers(), other.getAnswers()) && 
			nullSafeEquals(getText(), other.getText()) && 
			nullSafeEquals(getRichText(), other.getRichText()) ; 

	}
	
	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

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
		
	};
	
	public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

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
;

}
