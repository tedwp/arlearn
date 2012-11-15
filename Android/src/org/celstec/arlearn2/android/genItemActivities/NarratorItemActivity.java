package org.celstec.arlearn2.android.genItemActivities;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.ResponseCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
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
//	protected TextView descriptionTextView;
	protected NarratorItem narratorBean;
	protected Button provideAnswerButton;

	private TreeSet<Response> resp;
	
	protected GenericMessageListAdapter adapter;

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
//		descriptionTextView = (TextView) findViewById(R.id.giNarratorDescription);
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
//		DBAdapter db = new DBAdapter(this);
//		db.openForRead();
//		narratorBean =  (NarratorItem) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).queryById(narratorBean.getId(),menuHandler.getPropertiesAdapter().getCurrentRunId());
//		db.close();
//
	}

	protected void loadDataToGui() {
		if (narratorBean.getRichText() != null) {
			String html = narratorBean.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
//			webview.loadData(html, "text/html", "utf-8");
		} else {
			webview.setVisibility(View.GONE);
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

//		DBAdapter db = new DBAdapter(this);
//		db.openForRead();
		Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
		Long itemId = narratorBean.getId();
		resp =ResponseCache.getInstance().getResponses(runId, itemId);
		if (resp == null) {
//			ResponseCache.getInstance().reloadFromDb(runId, itemId, this);
			return;
		}
		
//		resp = ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).query(getMenuHandler().getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
		
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
//		db.close();
		
		LinearLayout listView = (LinearLayout) findViewById(R.id.narratoranswerlist); 
//		adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, users);
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
//		listView.setAdapter(adapter);
	}
	
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		Intent i = new Intent(this, ViewAnswerActivity.class);
//		i.putExtra("response", resp[(int)id]);
//		startActivity(i);
//	}
	
	@Override
	protected void onResume() {
		super.onResume();
		renderAnswers();
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
