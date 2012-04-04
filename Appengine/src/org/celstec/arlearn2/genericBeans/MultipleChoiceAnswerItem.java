package org.celstec.arlearn2.genericBeans;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceAnswerItem {
	private Boolean isCorrect;
	private String answer;

	public MultipleChoiceAnswerItem() {
	}
	
	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
