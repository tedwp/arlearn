package org.celstec.arlearn2.beans.generalItem;

import java.util.List;
import java.util.Vector;

public class MultipleChoiceTest extends GeneralItem {

	public static String answersType = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem";

	private List<MultipleChoiceAnswerItem> answers = new Vector();
	private String richText;
	private String text;
	
	public MultipleChoiceTest() {
		
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
	
	
}
