package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class OpenBadgeActivity extends GeneralActivity {

	OpenBadge badgeBean;
	protected WebView webview;
	protected Button provideAnswerButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());

		unpackDataFromIntent();
		getGuiComponents();
		loadDataToGui();

	}

	protected int getContentView() {
		return R.layout.gi_detail_badge;
	}

	protected void unpackDataFromIntent() {
		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		badgeBean = (OpenBadge) gi;
	}
	
	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);

	}

	protected void loadDataToGui() {

//		String html = "<html>";
//		html += "<head>";
//		html += "<script src=\"http://beta.openbadges.org/issuer.js\"></script>";
//		html += "</head>";
//		html += "</html>";
		webview.loadUrl("http://sharetec.celstec.org/sandbox/badges.html");
		webview.getSettings().setJavaScriptEnabled(true);
		
//		webview.loadUrl("javascript:alert('hallo'))");
		provideAnswerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webview.loadUrl("javascript:OpenBadges.issue(['http://sharetec.celstec.org/sandbox/badgeassertion.json'])");

			}
		});
//		webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
		if (badgeBean.getName() != null) {
			setTitle(badgeBean.getName());
		}

	}

	@Override
	public boolean isGenItemActivity() {
		return true;
	}
}
