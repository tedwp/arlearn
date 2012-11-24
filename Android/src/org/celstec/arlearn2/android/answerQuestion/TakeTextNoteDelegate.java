package org.celstec.arlearn2.android.answerQuestion;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TakeTextNoteDelegate {

	AnnotateActivity ctx;
	EditText et;

	public TakeTextNoteDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		et = (EditText) ctx.findViewById(R.id.editOpenQuestionText);
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				ctx.displayPublishButton();
				
			}
		});
	}

	public String getText() {
		if (et.getText() == null || et.getText().toString().equals("")) {
			return null;	
		}
		return et.getText().toString();
	}
}
