package org.celstec.arlearn2.android.genItemActivities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.broadcast.ResponseService;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.run.Response;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;


public class MultipleChoiceActivity extends GeneralActivity {

	HashMap<CheckBox, MultipleChoiceAnswerItem> buttonList = new HashMap<CheckBox, MultipleChoiceAnswerItem>();
	HashMap<String, MultipleChoiceAnswerItem> nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
	private Button submitVoteButton;
	protected WebView webview;
	protected TextView textView;

	private MultipleChoiceTest mct ;
	private List<MultipleChoiceAnswerItem> selected = new ArrayList<MultipleChoiceAnswerItem>();
	private long runId;
	private String account;
//	private PublishActionTask actionTask = new PublishActionTask(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
		account =getMenuHandler().getPropertiesAdapter().getUsername();
		setContentView(R.layout.gi_detail_multiplechoice);
		Long id = getIntent().getExtras().getLong("id"); //TODO make constant
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		mct = (MultipleChoiceTest) bean; //TODO check casting
		cancelNotification((int)mct.getId().longValue());
		 
		initUi(mct);
		fireReadAction(mct);
		submitVoteButton = (Button) findViewById(R.id.mct_submit);
		submitVoteButton.setEnabled(false);
		submitVoteButton.setOnClickListener(new OnClickListener(){

			
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
		
		// if (selected.getId() != null)
		// ActionDispatcher.publishAction(MultipleChoiceActivity.this,
		// "answer_"+selected.getId(), runId, account);

		try {
			for (MultipleChoiceAnswerItem sel: selected) {
				Response r = new Response();
				r.setUserEmail(getMenuHandler().getPropertiesAdapter().getUsername());
				r.setRunId(getMenuHandler().getPropertiesAdapter().getCurrentRunId());
				r.setTimestamp(System.currentTimeMillis());
				r.setGeneralItemId(mct.getId());
				JSONObject responseValueJson = new JSONObject();
				responseValueJson.put("isCorrect", sel.getIsCorrect());
				responseValueJson.put("answer", sel.getAnswer());
				if (sel.getId() != null) {
					PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
					ActionsDelegator.getInstance().publishAction(MultipleChoiceActivity.this, "answer_" + sel.getId(), pa.getCurrentRunId(), pa.getUsername(), mct.getId(), mct.getType());
					ActionsDelegator.getInstance().publishAction(MultipleChoiceActivity.this, "answer_given", pa.getCurrentRunId(), pa.getUsername(), mct.getId(), mct.getType());

				}
				r.setResponseValue(responseValueJson.toString());
				Intent intent = new Intent(MultipleChoiceActivity.this, ResponseService.class);
				intent.putExtra("bean", r);

				startService(intent);
			}
			

			MultipleChoiceActivity.this.finish();
		} catch (JSONException e) {
			Log.e("exception", e.getMessage(), e);
		}
	}

//	private MultipleChoiceTest readMctFromDb(String id){
//		DBAdapter db = new DBAdapter(MultipleChoiceActivity.this);
//		db.openForRead();
//		GeneralItem gi = (GeneralItem) db.table(DBAdapter.GENERALITEM_ADAPTER).queryById(id);
//		mct = new MultipleChoiceTest(gi);
//		db.close();
//		return mct;
//	}
	
	private void initUi(MultipleChoiceTest mct) {
		textView = ((TextView) findViewById(R.id.mct_question)); //.setText(mct.getQuestion())
		webview = (WebView) findViewById(R.id.mct_webview);
		
		if (mct.getRichText() != null) {
			String html = mct.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
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
			if (multipleChoiceAnswerItem.getNfcTag() != null) nfcMapping.put(multipleChoiceAnswerItem.getNfcTag(), multipleChoiceAnswerItem);
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
	        for (CheckBox buttonFromList: buttonList.keySet()) {
	        	if (buttonFromList.isChecked()) selected.add(buttonList.get(buttonFromList));
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

}
