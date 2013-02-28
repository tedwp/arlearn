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
	package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.client.ChannelClient;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
//		TextView descriptionTextView = (TextView) findViewById(R.id.giNarratorDescription);
//		descriptionTextView.setVisibility(View.GONE);
		Button provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
		provideAnswerButton.setText(getString(R.string.cancel));
		provideAnswerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NfcScanOnDemandActivity.this.finish();
				
			}
		});

		WebView webview = (WebView) findViewById(R.id.giNarratorWebView);
		String summary = "<html><body><br><br><br><center><b>"+getString(R.string.scanTag)+"</b></center></body></html>";
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
				ChannelClient.getChannelClient().pong(0, "", to, from, Ping.READ_NFC, action, timestamp);
			}
		}).start();
		NfcScanOnDemandActivity.this.finish();
	}
	
	public void onBroadcastMessage(Bundle bundle) {
		if (bundle.getBoolean(NFC_SCAN_SHUTDOWN, false)) this.finish();
	}
	
	public static final String NFC_SCAN_SHUTDOWN = "NfcScanShutdownMessage"; 
	

}
