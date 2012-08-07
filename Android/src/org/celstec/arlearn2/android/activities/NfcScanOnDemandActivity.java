package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.client.ChannelClient;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class NfcScanOnDemandActivity extends GeneralActivity {

	private String to;
	private String from;
	private long timestamp;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_detail_narratoritem);
		TextView descriptionTextView = (TextView) findViewById(R.id.giNarratorDescription);
		descriptionTextView.setVisibility(View.GONE);
		Button provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
		provideAnswerButton.setVisibility(View.GONE);

		WebView webview = (WebView) findViewById(R.id.giNarratorWebView);
		String summary = "<html><body><br><br><br><center><b>scan an NFC tag</b></center></body></html>";
		webview.loadData(summary, "text/html", "utf-8");
		
		to =  getIntent().getExtras().getString("to");
		from = getIntent().getExtras().getString("from");
		timestamp = getIntent().getExtras().getLong("timestamp");

	}
	
	
	@Override
	public boolean isGenItemActivity() {
		return false;
	}		

	protected void newNfcAction(final String action) {
		new Thread(new Runnable() {
			public void run() {
				ChannelClient.getChannelClient().pong(0, "", to, from, action, timestamp);
			}
		}).start();
		NfcScanOnDemandActivity.this.finish();
	}
	
	

}
