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
package org.celstec.arlearn2.android.genItemActivities;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.SingleChoiceImageActivity.PlayAudioTask;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MultipleChoiceImageActivity extends SingleChoiceImageActivity{

	private ArrayList<MultipleChoiceAnswerItem> selected = new ArrayList<MultipleChoiceAnswerItem>();
	private MultipleChoiceImageTest mct;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected OnClickListener createImageViewClickerListener(final String answerId, final ImageView im) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				MultipleChoiceAnswerItem selectedItem = null;
				for (MultipleChoiceAnswerItem mcai : getMultipleChoiceAnswers()) {
					if (mcai.getId().equals(answerId)) {
						selectedItem = mcai;
					}
				}
				if (!selected.contains(selectedItem)){
					new PlayAudioTask().execute(answerId + ":a");

				}

				toggleSelectedAnswer(selectedItem, im);
				submitVoteButton.setEnabled(!selected.isEmpty());
				
			}
		};
	}
	
	protected GeneralItem getBean() {
		return mct;
	}
	protected String getRichText() {
		return mct.getRichText();
	}
	protected List<MultipleChoiceAnswerItem> getMultipleChoiceAnswers () {
		return mct.getAnswers();
	}
	
	protected void setGeneralItemBean() {
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		mct = (MultipleChoiceImageTest) bean; // TODO check casting

	}
	private void toggleSelectedAnswer(MultipleChoiceAnswerItem selectedItem, ImageView im) {
		
		if (selected.contains(selectedItem)) {
			selected.remove(selectedItem);
			answerViewMapping.get(selectedItem).setBackgroundDrawable(null);
		} else {
			selected.add(selectedItem);
			answerViewMapping.get(selectedItem).setBackgroundDrawable(drawable);

		}
		
	}
	
	protected void submitButtonClick() {
		if (selected != null) {
			castResponse();

		}
	}
	
	private void castResponse() {
		for (MultipleChoiceAnswerItem sel : selected) {
			ResponseDelegator.getInstance().publishMultipleChoiceResponse(MultipleChoiceImageActivity.this, mct, sel);
		}
		MultipleChoiceImageActivity.this.finish();
	}
	
	@Override
	public GeneralItem getGeneralItem() {
		return mct;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		mct = (MultipleChoiceImageTest) gi; 
	}
	
}
