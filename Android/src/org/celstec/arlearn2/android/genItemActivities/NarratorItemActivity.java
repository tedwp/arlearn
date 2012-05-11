package org.celstec.arlearn2.android.genItemActivities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.GeneralListActivity;
import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.run.Response;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NarratorItemActivity extends GeneralListActivity {

	protected WebView webview;
	protected TextView descriptionTextView;
	protected NarratorItem narratorBean;

	private Response[] resp;
//	private String[] answersString = null;
	protected ListAdapter adapter;


	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
//		setContentView(getContentView());
		View headerView = getLayoutInflater().inflate(getContentView(), null);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.addHeaderView(headerView, null, false);
		
		unpackDataFromIntent();
		checkIfNotification();
		getGuiComponents();
		loadDataToGui();
		fireAction();

//			renderAnswers();
	}

//	public boolean isOpenQuestion() {
//		return narratorBean.getOpenQuestion() != null;
//	}

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
		// setContentView(R.layout.gi_detail_narratoritem);
//		if (isOpenQuestion()) {
			
//		} else {
//			setContentView(R.layout.gi_detail_narratoritem);
//		}
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		descriptionTextView = (TextView) findViewById(R.id.giNarratorDescription);
	}

	protected void unpackDataFromIntent() {
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		
		narratorBean = (NarratorItem) bean;
	}

	protected void loadDataToGui() {
		if (narratorBean.getRichText() != null) {
			String html = narratorBean.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
//			webview.loadData(html, "text/html", "utf-8");
		} else {
			webview.setVisibility(View.GONE);
		}
		if (narratorBean.getDescription() != null && !narratorBean.getDescription().trim().equals("")) {
			descriptionTextView.setText(narratorBean.getDescription());
		} else {
			descriptionTextView.setVisibility(View.GONE);
		}
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
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		resp = ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).query(getMenuHandler().getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
		db.close();
		
//		answersString = new String[] { "answer 1", "answer2" };
		final String[] matrix = { "_id", "title" };
		final String[] columns = { "title" };
		MatrixCursor mCursor = new MatrixCursor(matrix);
		SimpleDateFormat formatter = new SimpleDateFormat("d MMM - HH:mm:ss");
		if (resp != null) {
			for (int i = 0; i < resp.length; i++) {
				Date d = new Date(resp[i].getTimestamp());
//				Bitmap bMap = BitmapFactory.decodeFile("/sdcard/test2.png");
				
				mCursor.addRow(new String[] { "" + i, formatter.format(d) });
			}
		}
		startManagingCursor(mCursor);
		final int[] layouts = { R.id.messageItem1 };

		adapter = new SimpleCursorAdapter(this, R.layout.messageline, mCursor, columns, layouts);
		setListAdapter(adapter);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(this, ViewAnswerActivity.class);
		i.putExtra("response", resp[(int)id]);
		startActivity(i);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		renderAnswers();
	}
	
	public boolean isGenItemActivity() {
		return true;
	}

	@Override
	public boolean isMessage() {
		if (narratorBean == null) return false;
		return narratorBean.isMessage();
	}
	
}
