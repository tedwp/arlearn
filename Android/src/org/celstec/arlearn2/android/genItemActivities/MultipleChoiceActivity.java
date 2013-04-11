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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;

public class MultipleChoiceActivity extends GeneralItemActivity {

	HashMap<CheckBox, MultipleChoiceAnswerItem> buttonList = new HashMap<CheckBox, MultipleChoiceAnswerItem>();
	HashMap<String, MultipleChoiceAnswerItem> nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
	private Button submitVoteButton;
	protected WebView webview;
	protected TextView textView;

	private MultipleChoiceTest mct;
	protected String richText;

	private List<MultipleChoiceAnswerItem> selected = new ArrayList<MultipleChoiceAnswerItem>();

	// private PublishActionTask actionTask = new PublishActionTask(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_detail_multiplechoice);
		// cancelNotification((int) mct.getId().longValue());

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
		for (MultipleChoiceAnswerItem sel : selected) {
			ResponseDelegator.getInstance().publishMultipleChoiceResponse(MultipleChoiceActivity.this, mct, sel);
		}
		MultipleChoiceActivity.this.finish();
	}

	// private MultipleChoiceTest readMctFromDb(String id){
	// DBAdapter db = new DBAdapter(MultipleChoiceActivity.this);
	// db.openForRead();
	// GeneralItem gi = (GeneralItem)
	// db.table(DBAdapter.GENERALITEM_ADAPTER).queryById(id);
	// mct = new MultipleChoiceTest(gi);
	// db.close();
	// return mct;
	// }

	private void initUi(MultipleChoiceTest mct) {
		super.getGuiComponents();
		super.initCountdownView();
		textView = ((TextView) findViewById(R.id.mct_question)); // .setText(mct.getQuestion())
		webview = (WebView) findViewById(R.id.mct_webview);

		if (mct.getRichText() != null) {
			String html = mct.getRichText();
			if (!html.equals(richText)) {
				webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
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
		buttonList = new HashMap<CheckBox, MultipleChoiceAnswerItem>();
		nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
		while (it.hasNext()) {
			MultipleChoiceAnswerItem multipleChoiceAnswerItem = (MultipleChoiceAnswerItem) it.next();
			CheckBox rb = new CheckBox(this);
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
			selected = new ArrayList<MultipleChoiceAnswerItem>();
			for (CheckBox buttonFromList : buttonList.keySet()) {
				if (buttonFromList.isChecked())
					selected.add(buttonList.get(buttonFromList));
			}
		}
	};

	public boolean isGenItemActivity() {
		return true;
	}

	@Override
	protected void newNfcAction(String action) {
		if (nfcMapping.containsKey(action)) {
			selected.add(nfcMapping.get(action));
			castResponse();
		}
	}

	@Override
	public GeneralItem getGeneralItem() {
		return mct;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		mct = (MultipleChoiceTest) gi;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("richtText", richText);
	}

	protected void unpackBundle(Bundle inState) {
		super.unpackBundle(inState);
		richText = inState.getString("richtText");
	}

}
