package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;

import android.content.Intent;
import android.net.Uri;
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
		fireReadAction(badgeBean);
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
		PropertiesAdapter pa = PropertiesAdapter.getInstance(this);
		Intent intent = new Intent(
				Intent.ACTION_VIEW, 
				Uri.parse("http://ar-learn.appspot.com/issuebadge.jsp?runId="+pa.getCurrentRunId()+"&itemId="+badgeBean.getId()+"&email="+pa.getUsername()));
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		startActivity(intent);
		this.finish();
		if (badgeBean.getName() != null) {
			setTitle(badgeBean.getName());
		}
	}

	@Override
	public boolean isGenItemActivity() {
		return true;
	}
}
