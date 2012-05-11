package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class DetailViewActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_view);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		String summary = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"64\" height=\"38\" src=\"http://www.youtube.com/embed/iEmt_qnZKYY\" frameborder=\"0\">";
		myWebView.loadData(summary, "text/html", "utf-8");
	}

}
