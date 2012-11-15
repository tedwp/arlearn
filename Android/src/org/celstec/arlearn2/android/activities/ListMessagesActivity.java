package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.ActionReceiver;
import org.celstec.arlearn2.android.broadcast.GeneralItemReceiver;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.MessageListRecord;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ListMessagesActivity extends GeneralActivity implements ListitemClickInterface {

	private GeneralItem[] gis = null;
	private GenericMessageListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
		
		Intent gimIntent = new Intent();
		gimIntent.setAction(GeneralItemReceiver.action);
		sendBroadcast(gimIntent);
		
		Intent actionIntent = new Intent();
		actionIntent.setAction(ActionReceiver.action);
		sendBroadcast(actionIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadMessagesFromCache();
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
			Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
			if (runId == null || RunCache.getInstance().getRun(runId) == null) {
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
			MessageListRecord r = new MessageListRecord(gis[j], runId);
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
