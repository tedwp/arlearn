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
package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.IntentIntegrator;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.ScanTag;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class ScanTagActivity extends GeneralItemActivity {

	ScanTag scanTag;
	protected String richText;

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
			if (scanTag != null) {
				loadDataToGui();
			}
		}
	}

	protected void unpackDataFromIntent() {
		if (scanTag.getName() != null) {
			setTitle(scanTag.getName());
		}
		if (scanTag.getAutoLaunchQrReader() != null && scanTag.getAutoLaunchQrReader() == true) {
			fireBarcodeScanner();
		}
	}
	
	protected void getGuiComponents() {
		super.getGuiComponents();
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
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
	}
	protected void loadDataToGui() {
		super.initCountdownView();
		if (scanTag.getRichText() != null) {
			String html = scanTag.getRichText();
			if (!html.equals(richText)) {
				webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
				richText = html;
			}
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

	@Override
	public GeneralItem getGeneralItem() {
		return scanTag;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		scanTag = (ScanTag) gi;
	}
	
}
