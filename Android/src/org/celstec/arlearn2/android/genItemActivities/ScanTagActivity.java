package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.IntentIntegrator;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.ScanTag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;;

public class ScanTagActivity extends GeneralActivity {

	ScanTag scanTag;
	protected WebView webview;
	protected ImageView scanBarcodebutton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());

		unpackDataFromIntent();
		getGuiComponents();
		loadDataToGui();

		fireReadAction(scanTag);
	}

	protected int getContentView() {
		return R.layout.gi_detail_scantag;
	}
	
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
			if (runId == null || RunCache.getInstance().getRun(runId) == null) {
				this.finish();
			}
		loadDataToGui();
		}
	}

	protected void unpackDataFromIntent() {
		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		scanTag = (ScanTag) gi;
		if (scanTag.getName() != null) {
			setTitle(scanTag.getName());
		}
		if (scanTag.getAutoLaunchQrReader() != null && scanTag.getAutoLaunchQrReader() == true) {
			fireBarcodeScanner();
		}
	}
	
	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		scanBarcodebutton = (ImageView) findViewById(R.id.scanBarcode);
		scanBarcodebutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fireBarcodeScanner();
				
			}
		});
	}

	protected void fireBarcodeScanner() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(this);
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
	}
	protected void loadDataToGui() {
		if (scanTag.getRichText() != null) {
			String html = scanTag.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
		} else {
			webview.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean isGenItemActivity() {
		return true;
	}
	
	@Override
	protected void newNfcAction(String action) {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		ActionsDelegator.getInstance().publishAction(this, action, pa.getCurrentRunId(), pa.getUsername(), scanTag.getId(), scanTag.getClass().getName());
		this.finish();
	}
}
