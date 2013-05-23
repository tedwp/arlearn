package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import java.util.ArrayList;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;

import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SingleChoiceExtensionEditor  extends VLayout implements ExtensionEditor{

	protected  ArrayList<AnswerForm> forms = new ArrayList<AnswerForm>();
	

	
	public SingleChoiceExtensionEditor() {

	}
	
	public SingleChoiceExtensionEditor(GeneralItem gi) {
		this();
		SingleChoiceTest scTest = (SingleChoiceTest) gi;
		
		for (MultipleChoiceAnswer answer: scTest.getAnswers()) {
			AnswerForm aForm = new AnswerForm(answer, forms.size());
			forms.add(aForm);
			addMember(aForm);
		}
		addAdditionalAnswer();
		
	}

	@Override
	public void saveToBean(GeneralItem gi) {
		JSONArray array = new JSONArray();
		int i = 0;
		for (AnswerForm form: forms){
			MultipleChoiceAnswer answer = form.getMultipleChoiceAnswer();
			if (answer != null) array.set(i++, form.getMultipleChoiceAnswer().getJsonRep());
		}
		gi.getJsonRep().put("answers", array);
		

	}
	
	protected void addAdditionalAnswer() {
		AnswerForm aForm = new AnswerForm(null, forms.size());
		forms.add(aForm);
		addMember(aForm);
	}
	
	
	protected class AnswerForm extends DynamicForm {
		
		public AnswerForm(MultipleChoiceAnswer answer, final int position) {
			final TextItem answerItem = new TextItem("answer", "Answer "+(position+1));
			if (answer != null) answerItem.setValue(answer.getString("answer"));
			answerItem.setStartRow(true);
			answerItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});

			final CheckboxItem isCorrect = new CheckboxItem("isCorrect", "is correct");
			if (answer != null) isCorrect.setValue(answer.getBoolean("isCorrect"));
			isCorrect.setColSpan(1);
			
			HiddenItem hiddenId = new HiddenItem("id");
			if (answer != null) hiddenId.setValue(answer.getString("id"));

			setFields(answerItem, isCorrect, hiddenId);
			
			setNumCols(4);

		}
		
		public MultipleChoiceAnswer getMultipleChoiceAnswer() {
			MultipleChoiceAnswer result = new MultipleChoiceAnswer();
			String answerString = getValueAsString("answer");
			if (answerString == null || answerString.equals("")) return null;
			result.setString("answer", getValueAsString("answer"));
			if (getValueAsString("id") != null) result.setString("id", getValueAsString("id"));
			Boolean isCorrectValue = (Boolean) getValue("isCorrect");
			if (isCorrectValue == null) isCorrectValue = false;
			result.setBoolean("isCorrect", isCorrectValue); 
			
			return result;
		}
	}
	
	@Override
	public boolean validate() {
		return true;
	}

}
