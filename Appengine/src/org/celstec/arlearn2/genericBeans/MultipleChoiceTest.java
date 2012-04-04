package org.celstec.arlearn2.genericBeans;

import java.util.List;
import java.util.Vector;




public class MultipleChoiceTest extends OpenQuestion {
	
	private List<MultipleChoiceAnswerItem> answers = new Vector();;

	public MultipleChoiceTest() {
		super();
	}

	public List<MultipleChoiceAnswerItem> getAnswers() {
		return answers;
	}

	public void setAnswers(List<MultipleChoiceAnswerItem> answers) {
		this.answers = answers;
	}
	
	

}
