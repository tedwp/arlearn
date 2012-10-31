package org.celstec.arlearn2.android.genItemActivities;

import java.util.HashMap;
import java.util.Iterator;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.broadcast.ResponseService;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;
import org.celstec.arlearn2.android.sync.MyResponseSyncronizer;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Response;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;


public class MultipleChoiceActivity extends GeneralActivity {

	HashMap<RadioButton, MultipleChoiceAnswerItem> buttonList = new HashMap<RadioButton, MultipleChoiceAnswerItem>();
	HashMap<String, MultipleChoiceAnswerItem> nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
	private Button submitVoteButton;
	protected WebView webview;
	protected TextView textView;

	private MultipleChoiceTest mct ;
	private MultipleChoiceAnswerItem selected = null;
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
		Response r = new Response();
		r.setUserEmail(getMenuHandler().getPropertiesAdapter().getUsername());
		r.setRunId(getMenuHandler().getPropertiesAdapter().getCurrentRunId());
		r.setTimestamp(System.currentTimeMillis());
		r.setGeneralItemId(mct.getId());
		// if (selected.getId() != null)
		// ActionDispatcher.publishAction(MultipleChoiceActivity.this,
		// "answer_"+selected.getId(), runId, account);

		try {
			JSONObject responseValueJson = new JSONObject();
			responseValueJson.put("isCorrect", selected.getIsCorrect());
			responseValueJson.put("answer", selected.getAnswer());
			if (selected.getId() != null) {
				PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
				ActionDispatcher.publishAction(MultipleChoiceActivity.this, "answer_" + selected.getId(), pa.getCurrentRunId(), pa.getUsername(), mct.getId(), mct.getType());
				ActionDispatcher.publishAction(MultipleChoiceActivity.this, "answer_given", pa.getCurrentRunId(), pa.getUsername(), mct.getId(), mct.getType());

			}
			r.setResponseValue(responseValueJson.toString());
			Intent intent = new Intent(MultipleChoiceActivity.this, ResponseService.class);
			intent.putExtra("bean", r);

			startService(intent);

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
		buttonList = new HashMap<RadioButton, MultipleChoiceAnswerItem>();
		nfcMapping = new HashMap<String, MultipleChoiceAnswerItem>();
		while (it.hasNext()) {
			MultipleChoiceAnswerItem multipleChoiceAnswerItem = (MultipleChoiceAnswerItem) it.next();
			RadioButton rb = new RadioButton(this);
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
	        RadioButton buttonClicked = (RadioButton) viewClicked;
	        for (RadioButton buttonFromList: buttonList.keySet()) {
	        	if (buttonFromList!= buttonClicked) {
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
		// TODO Auto-generated method stub
		
	}
	
	
	//TODO find solution for this

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (!isGenItemActivity()) return super.onKeyDown(keyCode, event);
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			this.finish();
//			if (mct != null && mct.isMessage()) {
//				startActivity(new Intent(this, ListMessagesActivity.class));
//			} else {
//				startActivity(new Intent(this, MapViewActivity.class));
//
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
