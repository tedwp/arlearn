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
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

public class OpenBadgeActivity extends GeneralActivity {

	OpenBadge badgeBean;
	protected WebView webview;
	protected Button provideAnswerButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());

	
		getGuiComponents();
		loadDataToGui();
		fireReadAction(badgeBean);
	}

	protected int getContentView() {
		return R.layout.gi_detail_badge;
	}
	
	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
	}

	protected void loadDataToGui() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(this);
		Intent intent = new Intent(
				Intent.ACTION_VIEW, 
				Uri.parse("http://streetlearn.appspot.com/issuebadge.jsp?runId="+pa.getCurrentRunId()+"&itemId="+badgeBean.getId()+"&email="+pa.getFullId()));
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

	@Override
	public GeneralItem getGeneralItem() {
		return badgeBean;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		badgeBean = (OpenBadge) gi;		
	}
}
