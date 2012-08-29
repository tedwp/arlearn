package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.GeneralItemReceiver;
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

public class ListMessagesActivity extends GeneralActivity implements ListitemClickInterface{

	private GeneralItem[] gis = null;
	private long[] read = new long[0]; 
	private GenericMessageListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
		Intent gimIntent = new Intent();
		gimIntent.setAction(GeneralItemReceiver.action);
		sendBroadcast(gimIntent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		doDatabaseQueryOnAuthenticated();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onBroadcastMessage(Bundle bundle) {
		super.onBroadcastMessage(bundle);
		doDatabaseQueryOnAuthenticated();
	}

	private void doDatabaseQueryOnAuthenticated(){
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
			if (runId != null) {
				DBAdapter db = new DBAdapter(this);
				db.openForRead();
				gis = (GeneralItem[]) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).queryMessages(runId);
				read = (long[]) ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).queryReadItems(runId);

				db.close();
				renderMessagesList();
			}
		}
	}

	private void renderMessagesList() {
		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();

		ListView listView = (ListView) findViewById(R.id.listRuns); 

		adapter = new GenericMessageListAdapter(this,R.layout.listexcursionscreen, runsList);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
		for (int j = 0; j < gis.length; j++) {
			MessageListRecord r = new MessageListRecord(gis[j], read, mc);
			adapter.add(r);
		}
		db.close();
		
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		GIActivitySelector.startActivity(this, gis[position]);
		
	}
	
	public boolean isGenItemActivity() {
		return false;
	}
	
	public boolean showStatusLed() {
		return true;
	}

}
