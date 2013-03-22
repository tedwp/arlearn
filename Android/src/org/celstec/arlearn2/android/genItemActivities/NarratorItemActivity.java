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

import java.util.ArrayList;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ItemResponseListRecord;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.run.Response;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NarratorItemActivity extends GeneralActivity {

	protected WebView webview;
	protected TextView countDownTextView;
	protected NarratorItem narratorBean;
	protected String richText;
	protected Button provideAnswerButton;

	protected GenericMessageListAdapter adapter;

	private Handler mHandler = new Handler();

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

//		checkIfNotification();
		getGuiComponents();
		loadDataToGui();
		fireAction();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(counterTask);
	}
	
	@Override
	public GeneralItem getGeneralItem() {
		return narratorBean;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		narratorBean = (NarratorItem) gi;		
	}

	private Runnable counterTask = new Runnable() {
		public void run() {
			Long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
			if (disappearTime == null || disappearTime == -1) {
				mHandler.postDelayed(counterTask, 1000);
				return;
			}
			long millis = (disappearTime - System.currentTimeMillis());
			if (millis <= 0) {
				NarratorItemActivity.this.finish();
			}
			String defaultCountingText = "";
			int tens = ((int) millis / 100) % 10;
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;
			if (seconds < 10) {
				defaultCountingText = minutes + ":0" + seconds + "." + tens;
			} else {
				defaultCountingText = minutes + ":" + seconds + "." + tens;
			}
			if (countDownTextView != null)
				countDownTextView.setText(defaultCountingText);
			mHandler.postDelayed(counterTask, 100);

		}
	};

	private void fireAction() {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (narratorBean != null) {
			generalItemId = narratorBean.getId();
			generalItemType = narratorBean.getClass().getName();
		}
		ActionsDelegator.getInstance().publishAction(this, "read", pa.getCurrentRunId(), pa.getUsername(), generalItemId, generalItemType);

	}

//	protected void checkIfNotification() {
//		String ns = Context.NOTIFICATION_SERVICE;
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
//		mNotificationManager.cancel((int) narratorBean.getId().longValue());
//	}

	protected int getContentView() {
		return R.layout.gi_detail_narratoritem;
	}

	protected void getGuiComponents() {
		webview = (WebView) findViewById(R.id.giNarratorWebView);
		countDownTextView = (TextView) findViewById(R.id.timeLeftBeforeDisappear);
		provideAnswerButton = (Button) findViewById(R.id.provideAnswerButton);
		provideAnswerButton.setText(getString(R.string.ao_answer_menu));
		if (narratorBean.getOpenQuestion() != null) {
			provideAnswerButton.setVisibility(View.VISIBLE);

			provideAnswerButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NarratorItemActivity.this, AnswerQuestionActivity.class);
					intent.putExtra("runId", menuHandler.getPropertiesAdapter().getCurrentRunId());
					intent.putExtra("bean", ((NarratorItemActivity) NarratorItemActivity.this).getNarratorBean());
					intent.putExtra("generalItemId", ((NarratorItemActivity) NarratorItemActivity.this).getItemId());
					NarratorItemActivity.this.startActivity(intent);

				}
			});

		} else {
			provideAnswerButton.setVisibility(View.GONE);
		}
	}

//	protected void unpackDataFromIntent() {
//		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
//		narratorBean = (NarratorItem) bean;
//	}

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
		if (countDownTextView != null && narratorBean.getShowCountDown() != null && narratorBean.getShowCountDown()) {
			long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), narratorBean.getId());
			if (disappearTime == -1) {
				countDownTextView.setVisibility(View.GONE);
			} else {
				countDownTextView.setVisibility(View.VISIBLE);

			}
		} else {
			if (countDownTextView != null)
				countDownTextView.setVisibility(View.GONE);
		}
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
		ArrayList<GenericListRecord> users = new ArrayList<GenericListRecord>();

		for (Response response : resp) {
			GenericListRecord r = new ItemResponseListRecord(getMenuHandler().getPropertiesAdapter().getCurrentRunId(), response);
			users.add(r);
		}

		LinearLayout listView = (LinearLayout) findViewById(R.id.narratoranswerlist);
		adapter = new GenericMessageListAdapter(this, getContentView(), users);
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
		mHandler.postDelayed(counterTask, 1000);
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
