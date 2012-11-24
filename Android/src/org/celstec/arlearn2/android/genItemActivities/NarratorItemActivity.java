package org.celstec.arlearn2.android.genItemActivities;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.ResponseCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NarratorItemActivity extends GeneralActivity {

	protected WebView webview;
	protected TextView countDownTextView;
	protected NarratorItem narratorBean;
	protected Button provideAnswerButton;

	private TreeSet<Response> resp;
	
	protected GenericMessageListAdapter adapter;

	private Handler mHandler = new Handler();

	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
			if (runId == null || RunCache.getInstance().getRun(runId) == null) {
				this.finish();
			}
			reloadBeanFromDb();
			getGuiComponents();
			loadDataToGui();
			renderAnswers();
			if (narratorBean != null && !narratorBean.getId().equals(bundle.getLong(ActivityUpdater.ITEM_NO_TO_CLOSE, narratorBean.getId()))) {
				this.finish();
			}
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		
		unpackDataFromIntent();
		checkIfNotification();
		getGuiComponents();
		loadDataToGui();
		fireAction();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(counterTask);
	}
	
	private Runnable counterTask = new Runnable() {
		public void run() {
			Long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
			if (disappearTime == null || disappearTime == -1) {
				mHandler.removeCallbacks(counterTask);
				return;
			}
			long millis = (disappearTime - System.currentTimeMillis());
			if (millis <= 0) {
				NarratorItemActivity.this.finish();
			}
			String defaultCountingText = "";
			int tens = ((int)millis / 100) %10;
		       int seconds = (int) (millis / 1000);
		       int minutes = seconds / 60;
		       seconds     = seconds % 60;
		       if (seconds < 10) {
		    	   defaultCountingText = minutes+":0"+seconds+"."+tens;
		       } else {
		    	   defaultCountingText = minutes+":"+seconds+"."+tens;
		       }
			countDownTextView.setText(defaultCountingText);
		     mHandler.postDelayed(counterTask, 100);		    	   

		}
	};

	private void fireAction() {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (narratorBean != null) {
			generalItemId = narratorBean.getId();
			generalItemType = narratorBean.getClass().getName();
		} 
		ActionDispatcher.publishAction(this, "read", pa.getCurrentRunId(), pa.getUsername(), generalItemId, generalItemType);

	}

	protected void checkIfNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel((int) narratorBean.getId().longValue());
	}

	protected int getContentView() {
		return R.layout.gi_detail_narratoritem;
	}
	
	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		countDownTextView = (TextView) findViewById(R.id.timeLeftBeforeDisappear);
		provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
		provideAnswerButton.setText(getString(R.string.ao_answer_menu));
		if (narratorBean.getOpenQuestion() != null) {
			provideAnswerButton.setVisibility(View.VISIBLE);

			provideAnswerButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NarratorItemActivity.this, AnswerQuestionActivity.class);
					intent.putExtra("runId", menuHandler.getPropertiesAdapter().getCurrentRunId());
					intent.putExtra("bean", ((NarratorItemActivity) NarratorItemActivity.this).getNarratorBean());
					intent.putExtra("generalItemId", ((NarratorItemActivity) NarratorItemActivity.this).getItemId());
					NarratorItemActivity.this.startActivity(intent);
					
				}
			});
			
		} else {
			provideAnswerButton.setVisibility(View.GONE);
		}
	}

	protected void unpackDataFromIntent() {
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		narratorBean = (NarratorItem)  bean;
	}
	
	private void reloadBeanFromDb() {
		narratorBean =  (NarratorItem) GeneralItemsCache.getInstance().getGeneralItems(narratorBean.getId());
	}

	protected void loadDataToGui() {
		if (narratorBean.getRichText() != null) {
			String html = narratorBean.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
//			webview.loadData(html, "text/html", "utf-8");
		} else {
			webview.setVisibility(View.GONE);
		}
		if (narratorBean.getShowCountDown() != null && narratorBean.getShowCountDown()) {
			long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
			if (disappearTime == -1) {
				countDownTextView.setVisibility(View.INVISIBLE);	
			} else {
				countDownTextView.setVisibility(View.VISIBLE);
				
			}
		} else {
			countDownTextView.setVisibility(View.INVISIBLE);
		}
//		if (narratorBean.getDescription() != null && !narratorBean.getDescription().trim().equals("")) {
//			descriptionTextView.setText(narratorBean.getDescription());
//		} else {
//			descriptionTextView.setVisibility(View.GONE);
//		}
		if (narratorBean.getName() != null) {
			setTitle(narratorBean.getName());
		}
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated() && narratorBean.getOpenQuestion() != null) {
			menu.add(0, MenuHandler.PROVIDE_ANSWER, 0, getString(R.string.ao_answer_menu));
		} else {
			return false;
		}
		return true;
	}

	public Long getItemId() {
		if (narratorBean == null)
			return null;
		return narratorBean.getId();
	}
	
	public NarratorItem getNarratorBean() {
		return narratorBean;
	}
	
	
	
	private void renderAnswers() {
		Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
		Long itemId = narratorBean.getId();
		resp =ResponseCache.getInstance().getResponses(runId, itemId);
		if (resp == null) {
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("d MMM - HH:mm:ss");
		ArrayList<GenericListRecord> users = new ArrayList<GenericListRecord>();

		if (resp != null) {

			for (Response response: resp) {
//				for (int i = 0; i < resp.length; i++) {
				GenericListRecord r = new GenericListRecord();
				r.setImageResourceId(R.drawable.cloud_up_48);
				Date d = new Date(response.getTimestamp());
				double percentage = 0;
				int dividePercentageBy = 0;
				try {
					JSONObject json = new JSONObject(response.getResponseValue());
					int statusAudio = 10;
					
					if (json.has("audioUrl")) {
						String audioUrl = json.getString("audioUrl");
						statusAudio = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getReplicationStatus(audioUrl);
						percentage += org.celstec.arlearn2.android.cache.MediaCache.getInstance().getPercentageUploaded(audioUrl);
						dividePercentageBy++;
					}
					if (json.has("text")) {
						statusAudio =org.celstec.arlearn2.android.db.MediaCache.REP_STATUS_DONE;
						percentage =1;
						dividePercentageBy++;
					}
					
					int statusImage = 10;
					if (json.has("imageUrl")) {
						String imageUrl = json.getString("imageUrl");
						statusImage = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getReplicationStatus(imageUrl);
						percentage += org.celstec.arlearn2.android.cache.MediaCache.getInstance().getPercentageUploaded(imageUrl);
						dividePercentageBy++;
					}
					if (json.has("videoUrl")) {
						String videoUrl = json.getString("videoUrl");
						statusImage = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getReplicationStatus(videoUrl);
						percentage += org.celstec.arlearn2.android.cache.MediaCache.getInstance().getPercentageUploaded(videoUrl);
						dividePercentageBy++;
					}
					
						percentage /= dividePercentageBy;
					

					if (statusImage != 10 || statusAudio != 10) {
						int status = Math.min(statusAudio, statusImage);
						switch (status) {
						case MediaCache.REP_STATUS_TODO:
							r.setImageResourceId(R.drawable.cloud_up_48);
							break;
						case MediaCache.REP_STATUS_SYNCING:
							r.setImageResourceId(R.drawable.cloud_sync_48);
							break;
						case MediaCache.REP_STATUS_DONE:
							r.setImageResourceId(R.drawable.cloud_ok_48);
							break;

						default:
							break;
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				r.setMessageHeader(""+formatter.format(d));
				if (percentage == 1){
					r.setMessageDetail(null);
				} else{
					r.setMessageDetail(MessageFormat.format("{0,number,#.##%}", percentage));	
				}
				

				users.add(r);
			}
		}
		
		LinearLayout listView = (LinearLayout) findViewById(R.id.narratoranswerlist); 
		adapter = new GenericMessageListAdapter(this,getContentView(), users);
		listView.removeAllViews();
		for (int i = 0 ; i< adapter.getCount(); i++) {
			View v = adapter.getView(i, null, listView);
			listView.addView(v);
			final int id = i;
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(NarratorItemActivity.this, ViewAnswerActivity.class);
					i.putExtra("response", resp.toArray(new Response[]{})[(int)id]);
					startActivity(i);
					
				}
			});
			
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		renderAnswers();
	     mHandler.postDelayed(counterTask, 100);		    	   

	}
	
	
	public boolean isGenItemActivity() {
		return true;
	}
	
	public boolean isMessage() {
		if (narratorBean == null) return false;
		return narratorBean.isMessage();
	}
	
	public boolean showStatusLed() {
		return true;
	}
}
