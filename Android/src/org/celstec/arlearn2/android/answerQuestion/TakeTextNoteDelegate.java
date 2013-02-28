/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
