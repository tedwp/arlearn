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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.answerQuestion.DataCollectorDelegate;
import org.celstec.arlearn2.android.answerQuestion.DataCollectorDelegateManager;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.db.RegisterUploadInDbTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileSyncTask;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ItemResponseListRecord;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NarratorItemActivity extends GeneralItemActivity {

	protected WebView webview;
	protected NarratorItem narratorBean;
	protected String richText;
//	protected Button provideAnswerButton;

	protected GenericMessageListAdapter adapter;


	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
			if (runId == null || RunCache.getInstance().getRun(runId) == null) {
				this.finish();
			}
			if (narratorBean != null) {
				reloadBeanFromDb();
				getGuiComponents();
				loadDataToGui();
				renderAnswers();
				if (narratorBean != null && !narratorBean.getId().equals(bundle.getLong(ActivityUpdater.ITEM_NO_TO_CLOSE, narratorBean.getId()))) {
					this.finish();
				}
			} else {
				Long gameId = RunCache.getInstance().getGameId(runId);
				if (gameId != null)  {
					GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, gameId, runId);
				} else {
					this.finish();
				}
			}
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		getGuiComponents();
		loadDataToGui();
		fireAction();
	}
	
	@Override
	public GeneralItem getGeneralItem() {
		return narratorBean;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		narratorBean = (NarratorItem) gi;		
	}

	

	private void fireAction() {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (narratorBean != null) {
			generalItemId = narratorBean.getId();
			generalItemType = narratorBean.getClass().getName();
		}
		ActionsDelegator.getInstance().publishAction(this, "read", pa.getCurrentRunId(), pa.getFullId(), generalItemId, generalItemType);

	}

	protected int getContentView() {
		return R.layout.gi_detail_narratoritem;
	}

	protected void getGuiComponents() {
		super.getGuiComponents();
		final long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
		final String account = menuHandler.getPropertiesAdapter().getFullId();
		webview = (WebView) findViewById(R.id.giNarratorWebView);
//		provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
//		provideAnswerButton.setText(getString(R.string.ao_answer_menu));
		LinearLayout dcLayout = (LinearLayout) findViewById(R.id.datacollectionbar);
		ImageView pictureView = (ImageView) findViewById(R.id.picture_button);
		ImageView audioView = (ImageView) findViewById(R.id.speech_button);
		OpenQuestion oq = narratorBean.getOpenQuestion();
		if (oq != null) {
//			provideAnswerButton.setVisibility(View.VISIBLE);
//
//			provideAnswerButton.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(NarratorItemActivity.this, AnswerQuestionActivity.class);
//					intent.putExtra("runId", runId);
//					intent.putExtra("bean", ((NarratorItemActivity) NarratorItemActivity.this).getNarratorBean());
//					intent.putExtra("generalItemId", ((NarratorItemActivity) NarratorItemActivity.this).getItemId());
//					NarratorItemActivity.this.startActivity(intent);
//
//				}
//			});
//			if (PropertiesAdapter.getInstance(this).getAccountLevel() == Account.ADMINISTRATOR) {
				dcLayout.setVisibility(View.VISIBLE);
				dataCollectorManager = new DataCollectorDelegateManager(oq, this, runId, account);
				if (oq.isWithAudio()) {
					(audioView).setImageDrawable(getResources().getDrawable(R.drawable.dc_voice_search_128));
				}
//			}
		} else {
//			provideAnswerButton.setVisibility(View.GONE);
			dcLayout.setVisibility(View.GONE);
		}
	}
	
	
	private DataCollectorDelegateManager dataCollectorManager;
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dataCollectorManager.processResult(requestCode, data);
		if (resultCode == -1 && requestCode == 1) {
			onActivityResult(data);
		}
		
	}
	public void onActivityResult(Intent data) {
		
	}
	
	

	private void reloadBeanFromDb() {
		NarratorItem ni = (NarratorItem) GeneralItemsCache.getInstance().getGeneralItems(narratorBean.getId());
		if (ni != null) {
			narratorBean = ni;
		}
	}

	protected void loadDataToGui() {
		if (narratorBean.getRichText() != null) {
			String html = narratorBean.getRichText();
			if (!html.equals(richText)) {
				webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
				richText = html;
			}
		} else {
			webview.setVisibility(View.GONE);
		}
		super.initCountdownView();
		if (narratorBean.getName() != null) {
			setTitle(narratorBean.getName());
		}

	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("richtText", richText);
	}

	protected void unpackBundle(Bundle inState) {
		super.unpackBundle(inState);
		richText = inState.getString("richtText");
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated() && narratorBean.getOpenQuestion() != null) {
			menu.add(0, MenuHandler.PROVIDE_ANSWER, 0, getString(R.string.ao_answer_menu));
		} else {
			return false;
		}
		return true;
	}

	public Long getItemId() {
		if (narratorBean == null)
			return null;
		return narratorBean.getId();
	}

	public NarratorItem getNarratorBean() {
		return narratorBean;
	}

	private void renderAnswers() {
		final TreeSet<Response> resp = GeneralItemsDelegator.getInstance().getResponses(getMenuHandler().getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
		if (resp == null) {
			return;
		}
		ArrayList<GenericListRecord> recordArrayList = new ArrayList<GenericListRecord>();

		for (Response response : resp) {
			GenericListRecord r = new ItemResponseListRecord(getMenuHandler().getPropertiesAdapter().getCurrentRunId(), response);
			recordArrayList.add(r);
		}

		LinearLayout listView = (LinearLayout) findViewById(R.id.narratoranswerlist);
		adapter = new GenericMessageListAdapter(this, getContentView(), recordArrayList);
		listView.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View v = adapter.getView(i, null, listView);
			listView.addView(v);
			final int id = i;
			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(NarratorItemActivity.this, ViewAnswerActivity.class);
					i.putExtra("response", resp.toArray(new Response[] {})[(int) id]);
					startActivity(i);

				}
			});

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		renderAnswers();
	}

	public boolean isGenItemActivity() {
		return true;
	}

	public boolean isMessage() {
		if (narratorBean == null)
			return false;
		return narratorBean.isMessage();
	}

	public boolean showStatusLed() {
		return true;
	}

	
}
