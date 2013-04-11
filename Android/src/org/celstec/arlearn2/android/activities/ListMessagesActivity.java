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
import java.util.Iterator;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.MessageListRecord;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListMessagesActivity extends GeneralActivity implements ListitemClickInterface {

	private GeneralItem[] gis = null;
	private GenericMessageListAdapter adapter;
	private long runId = -1;
	private Long gameId = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
		
		ActionsDelegator.getInstance().synchronizeActionsWithServer(this);
		runId = PropertiesAdapter.getInstance(this).getCurrentRunId();

		RunDelegator.getInstance().loadRun(this, runId);
		ResponseDelegator.getInstance().synchronizeResponsesWithServer(this, runId);
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("runId", runId);
		outState.putLong("gameId", gameId);
	}

	protected void unpackBundle(Bundle inState) {
		super.unpackBundle(inState);
		runId = inState.getLong("runId");
		gameId = inState.getLong("gameId");		
	}
	

	@Override
	protected void onResume() {
		gameId = RunCache.getInstance().getGameId(runId);
		if (gameId != null) {
			GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, runId, gameId);
			loadMessagesFromCache();
			super.onResume();
		} else {
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render)
			loadMessagesFromCache();
	}

	private void loadMessagesFromCache() {
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			if (RunCache.getInstance().getRun(runId) == null) {
				this.finish();
			} else 	{
				TreeSet<GeneralItem> gil = GeneralItemVisibilityCache.getInstance().getAllVisibleMessages(runId);
				if (gil != null) {
					gis = new GeneralItem[gil.size()];
					int i = 0;
					for (Iterator<GeneralItem> iterator = gil.iterator(); iterator.hasNext();) {
						gis[i++] = (GeneralItem) iterator.next();
					}
					renderMessagesList();
				}
			}
		}
	}

	private void renderMessagesList() {
		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
		Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();

		ListView listView = (ListView) findViewById(R.id.listRuns);
		adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsList);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);
		for (int j = 0; j < gis.length; j++) {
			MessageListRecord r = new MessageListRecord(gis[j], runId, this);
			adapter.add(r);
		}
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		GIActivitySelector.startActivity(this, gis[position]);
	}
	
	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}

	public boolean isGenItemActivity() {
		return false;
	}

	public boolean showStatusLed() {
		return true;
	}
	
}
