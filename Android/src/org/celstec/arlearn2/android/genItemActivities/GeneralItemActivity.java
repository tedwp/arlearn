package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralListActivity;
import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.celstec.arlearn2.android.db.beans.NarratorItem;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GeneralItemActivity extends GeneralListActivity {

	protected WebView webview;
	protected TextView descriptionTextView;
	protected GeneralItem bean;
	protected ListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
		View headerView = getLayoutInflater().inflate(getContentView(), null);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.addHeaderView(headerView, null, false);

		checkIfNotification();
		unpackDataFromIntent();
		getGuiComponents();
		loadDataToGui();
		// fireAction();

		// if (isOpenQuestion())
		// renderAnswers();
	}

	protected int getContentView() {
		return R.layout.gi_detail_narratoritem;
	}

	protected void checkIfNotification() {
		int notId = getIntent().getExtras().getInt("notId", -1);
		if (notId != -1) {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			mNotificationManager.cancel(notId);
			mNotificationManager.cancel(1);
		}
	}

	protected void unpackDataFromIntent() {
		bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
	}

	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		descriptionTextView = (TextView) findViewById(R.id.giNarratorDescription);
	}

	protected void loadDataToGui() {
		descriptionTextView.setText("description works");
		if (bean.getName() != null) {
			setTitle(bean.getName());
		}
	}
	
	
	public boolean isGenItemActivity() {
		return true;
	}

	@Override
	public boolean isMessage() {
		if (bean == null) return false;
		return bean.isMessage();
	}

}