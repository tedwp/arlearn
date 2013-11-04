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

import java.util.HashMap;
import java.util.Iterator;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceTest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class SingleChoiceActivity extends GeneralItemActivity {

	HashMap<RadioButton, MultipleChoiceAnswerItem> buttonList = new HashMap<RadioButton, MultipleChoiceAnswerItem>();
	HashMap<String, MultipleChoiceAnswerItem> nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
	private Button submitVoteButton;
	protected WebView webview;
	protected TextView textView;

	private SingleChoiceTest mct;
	protected String richText;

	private MultipleChoiceAnswerItem selected = null;


    public void onBroadcastMessage(Bundle bundle, boolean render) {
        super.onBroadcastMessage(bundle, render);
        if (render) {
            Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
            if (runId == null || RunCache.getInstance().getRun(runId) == null) {
                this.finish();
            }
            super.initCountdownView();
        }
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_detail_multiplechoice);

		initUi(mct);
		fireReadAction(mct);
		submitVoteButton = (Button) findViewById(R.id.mct_submit);
		submitVoteButton.setEnabled(false);
		submitVoteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (selected != null) {
					castResponse();

				}
			}
		});
		if (!nfcMapping.isEmpty()) {
			submitVoteButton.setVisibility(View.GONE);
		}

	}

	private void castResponse() {
		ResponseDelegator.getInstance().publishMultipleChoiceResponse(SingleChoiceActivity.this, mct, selected);
        ResponseDelegator.getInstance().publishMultipleChoiceResponse(SingleChoiceActivity.this, mct, selected.getIsCorrect());
		SingleChoiceActivity.this.finish();
	}


	private void initUi(SingleChoiceTest mct) {
		super.getGuiComponents();
		super.initCountdownView();
		textView = ((TextView) findViewById(R.id.mct_question)); // .setText(mct.getQuestion())
		webview = (WebView) findViewById(R.id.mct_webview);

		if (mct.getRichText() != null) {
			String html = mct.getRichText();
			if (!html.equals(richText)) {
                String incommingFile = MediaFolders.getIncommingFilesDir().getAbsolutePath();
                Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
                if (runId != null) {
                    Long gameId = RunCache.getInstance().getGameId(runId);
                    incommingFile = "file:///"+incommingFile+"/"+gameId+"/";

                }
                webview.loadDataWithBaseURL(incommingFile, html, "text/html", "utf-8", null);
//				webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
				richText = html;
			}
		} else {
			webview.setVisibility(View.GONE);
		}
		
		if (mct.getText() != null) {
			textView.setText(mct.getText());
		} else {
			textView.setVisibility(View.GONE);
		}
		if (mct.getName() != null) {
			setTitle(mct.getName());
		}

		LinearLayout ll = (LinearLayout) findViewById(R.id.radioButtonLinearLayout);
		Iterator<MultipleChoiceAnswerItem> it = mct.getAnswers().iterator();
		buttonList = new HashMap<RadioButton, MultipleChoiceAnswerItem>();
		nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
		while (it.hasNext()) {
			MultipleChoiceAnswerItem multipleChoiceAnswerItem = (MultipleChoiceAnswerItem) it.next();
			RadioButton rb = new RadioButton(this);
			rb.setTextColor(Color.BLACK);
			buttonList.put(rb, multipleChoiceAnswerItem);
			if (multipleChoiceAnswerItem.getNfcTag() != null)
				nfcMapping.put(multipleChoiceAnswerItem.getNfcTag(), multipleChoiceAnswerItem);
			rb.setText(multipleChoiceAnswerItem.getAnswer());
			rb.setOnClickListener(radio_listener);
			ll.addView(rb);
		}
		if (!nfcMapping.isEmpty()) {
			ll.setVisibility(View.GONE);
		}
	}

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View viewClicked) {
			submitVoteButton.setEnabled(true);
			RadioButton buttonClicked = (RadioButton) viewClicked;
			for (RadioButton buttonFromList : buttonList.keySet()) {
				if (buttonFromList != buttonClicked) {
					buttonFromList.setChecked(false);
				} else {
					selected = buttonList.get(buttonClicked);
				}
			}
		}
	};

	public boolean isGenItemActivity() {
		return true;
	}

	@Override
	protected void newNfcAction(String action) {
		if (nfcMapping.containsKey(action)) {
			selected = nfcMapping.get(action);
			castResponse();
		}
	}

	@Override
	public GeneralItem getGeneralItem() {
		return mct;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		mct = (SingleChoiceTest) gi;
	}

	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("richtText", richText);
	}

	protected void unpackBundle(Bundle inState) {
		super.unpackBundle(inState);
		richText = inState.getString("richtText");
	}
}
