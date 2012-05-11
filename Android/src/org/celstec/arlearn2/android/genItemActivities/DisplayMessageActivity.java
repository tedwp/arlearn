package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.beans.GeneralItem;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

public class DisplayMessageActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_message);
		WebView webview = (WebView) findViewById(R.id.webView);
		
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("bean");
		int notId = getIntent().getExtras().getInt("notId");
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(notId);
		String summary = "<html><body>"+bean.getName()+"</body></html>";
		webview.loadData(summary, "text/html", "utf-8");

	}

}
