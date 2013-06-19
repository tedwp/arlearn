package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import java.util.ArrayList;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceImage;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor.AnswerForm;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor.UUID;

import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SingleChoiceImageExtensionEditor extends VLayout implements ExtensionEditor{

	protected  ArrayList<AnswerForm> forms = new ArrayList<AnswerForm>();

	public SingleChoiceImageExtensionEditor() {

	}
	
	public SingleChoiceImageExtensionEditor(GeneralItem gi) {
		this();
		SingleChoiceImage scTest = (SingleChoiceImage) gi;
		
		for (MultipleChoiceImageAnswerItem answer: scTest.getAnswers()) {
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
			MultipleChoiceImageAnswerItem answer = form.getMultipleChoiceAnswer();
			if (answer != null) array.set(i++, answer.getJsonRep());
		}
		gi.getJsonRep().put("answers", array);
		

	}
	
	@Override
	public boolean validate() {
		return true;
	}
	
	protected void addAdditionalAnswer() {
		AnswerForm aForm = new AnswerForm(null, forms.size());
		forms.add(aForm);
		addMember(aForm);
	}
	
	protected class AnswerForm extends DynamicForm {
		
		public AnswerForm(MultipleChoiceImageAnswerItem answer, final int position) {
			setGroupTitle("answer "+(position+1));
			setIsGroup(true);
			
			final TextItem imageItem = new TextItem(MultipleChoiceImageAnswerItem.IMAGE_URL, "picture url "+" "+(position+1));
			if (answer != null) imageItem.setValue(answer.getString(MultipleChoiceImageAnswerItem.IMAGE_URL));
			
			imageItem.setStartRow(true);
			imageItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});
			
			final TextItem audioItem = new TextItem(MultipleChoiceImageAnswerItem.AUDIO_URL, "audio url ");
			if (answer != null) audioItem.setValue(answer.getString(MultipleChoiceImageAnswerItem.AUDIO_URL));
			
			audioItem.setStartRow(true);
			audioItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});


			final CheckboxItem isCorrect = new CheckboxItem("isCorrect", "is Correct");
			if (answer != null) isCorrect.setValue(answer.getBoolean("isCorrect"));
			isCorrect.setColSpan(1);
			
			HiddenItem hiddenId = new HiddenItem("id");
			if (answer != null) hiddenId.setValue(answer.getString("id"));

			setFields(imageItem, audioItem, isCorrect, hiddenId);
			
			setNumCols(4);

		}
		
		public MultipleChoiceImageAnswerItem getMultipleChoiceAnswer() {
			MultipleChoiceImageAnswerItem result = new MultipleChoiceImageAnswerItem();
			String imageUrlString = getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_URL);
			if (imageUrlString == null || imageUrlString.equals("")) return null;
			result.setString(MultipleChoiceImageAnswerItem.IMAGE_URL, getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_URL));
			
			String audioUrlString = getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_URL);
			if (audioUrlString == null || audioUrlString.equals("")) return null;
			result.setString(MultipleChoiceImageAnswerItem.AUDIO_URL, getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_URL));
			if (getValueAsString("id") != null && !getValueAsString("id").equals("")) {
				result.setString("id", getValueAsString("id"));
			} else {
				result.setString("id", UUID.uuid(15));
			}
			Boolean isCorrectValue = (Boolean) getValue("isCorrect");
			if (isCorrectValue == null) isCorrectValue = false;
			result.setBoolean("isCorrect", isCorrectValue); 
			
			return result;
		}
	}
}
