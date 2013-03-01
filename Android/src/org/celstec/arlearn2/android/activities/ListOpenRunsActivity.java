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

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.RunListRecord;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.client.RunClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListOpenRunsActivity extends GeneralActivity implements ListitemClickInterface {
	public static final String TAG_ID = "tagId";
	private String tagId = null;
	private RunList rl = null;
	private ArrayList<GenericListRecord> runsRecordList;
	private ArrayList<Run> runsList;
	private boolean noRunMatches = true;

	private GenericMessageListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.addOpenRunTitle);
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			setContentView(R.layout.listexcursionscreen);

		}
		tagId = getIntent().getExtras().getString(TAG_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new QueryRuns().execute();
	}

	private void renderRunsList() {
		System.out.println("list is " + rl.getRuns());
		runsRecordList = new ArrayList<GenericListRecord>();
		runsList = new ArrayList<Run>();

		for (Run r: rl.getRuns()) {
			if (r.getDeleted() == null || !r.getDeleted()){
				if (RunDelegator.getInstance().getRun(r.getRunId())== null) {
					runsRecordList.add(new RunListRecord(r));
					runsList.add(r);	
				} else {
					noRunMatches = false;
				}
				
			}
				
			
		}
		ListView listView = (ListView) findViewById(R.id.listRuns);
		if (adapter == null || !adapter.isEqual(runsRecordList)) {
			adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsRecordList);
			adapter.setOnListItemClickCallback(this);
			listView.setAdapter(adapter);
		}
		if (runsList.isEmpty()) {
			if (noRunMatches) {
				Toast.makeText(ListOpenRunsActivity.this, getString(R.string.noMatchingRuns), Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(ListOpenRunsActivity.this, getString(R.string.noMoreMatchingRuns), Toast.LENGTH_LONG).show();

			}
			this.finish();
		}
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		Run run = runsList.get(position);
		new SelfRegisterRun().execute(run.getRunId());
	}
	
	@Override
	public boolean isGenItemActivity() {
		return false;
	}

	
	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}

	public class QueryRuns extends AsyncTask<Object, RunList, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			try {
				if (NetworkSwitcher.isOnline(ListOpenRunsActivity.this)) {
					RunList rl = RunClient.getRunClient().getRunsByTag(getMenuHandler().getPropertiesAdapter().getFusionAuthToken(), tagId);
					publishProgress(rl);
				} else {
					publishProgress();
				}
			} catch (Exception e) {
				publishProgress();

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(RunList... runList) {
			if (runList.length == 0) {
				Toast.makeText(ListOpenRunsActivity.this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			} else {
				rl = runList[0];
				renderRunsList();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	
	public class SelfRegisterRun extends AsyncTask<Long, Void, Void> {

		@Override
		protected Void doInBackground(Long... params) {
			try {
				if (NetworkSwitcher.isOnline(ListOpenRunsActivity.this)) {
					RunClient.getRunClient().selfRegister(getPropertiesAdapter().getFusionAuthToken(), params[0]);
				} else {
					publishProgress();
				}
			} catch (Exception e) {
				publishProgress();
			}
			return null;
		}
		
		
		@Override
		protected void onProgressUpdate(Void... voids) {
			if (voids.length == 0) {
				Toast.makeText(ListOpenRunsActivity.this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ListOpenRunsActivity.this.finish();
		}
		
	}
}
