package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImageAnswerItem;

public class MultipleChoiceImageExtensionEditor  extends SingleChoiceImageExtensionEditor{

	public MultipleChoiceImageExtensionEditor() {
		super();
	}
	
	public MultipleChoiceImageExtensionEditor(GeneralItem gi) {
		super();
		MultipleChoiceImage scTest = (MultipleChoiceImage) gi;
		
		for (MultipleChoiceImageAnswerItem answer: scTest.getAnswers()) {
			AnswerForm aForm = new AnswerForm(answer, forms.size());
			forms.add(aForm);
			addMember(aForm);
		}
		addAdditionalAnswer();
		
	}
}
