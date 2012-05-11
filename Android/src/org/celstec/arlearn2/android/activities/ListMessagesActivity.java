package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.GetLocationLessMessages;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

//import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.celstec.arlearn2.android.service.NotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ListMessagesActivity extends GeneralListActivity {

//	private ProgressDialog dialog;
//	private GetLocationLessMessages task;
	private GeneralItem[] gis = null;
	private long[] read = new long[0]; 
	private MessageListAdapter adapter;

//	private ListAdapter adapter;

	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			NotificationBean bean = (NotificationBean) intent.getExtras().getSerializable("bean");
			if (bean.getClass().equals(org.celstec.arlearn2.android.db.notificationbeans.GeneralItem.class)) {
				doDatabaseQueryOnAuthenticated();
			}
		}
	};
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doDatabaseQueryOnAuthenticated();
	}
	
	private void doDatabaseQueryOnAuthenticated(){
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			setContentView(R.layout.listexcursionscreen);
//			task = new GetLocationLessMessages(this);
//			task.execute(new Object[] {});
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
		adapter = new MessageListAdapter(this);
		adapter.emptyList();
		for (int j = 0; j < gis.length; j++) {
			String distance = "";
			MessageListAdapter.MessageLine ml = (adapter).new MessageLine(gis[j].getId(), gis[j].getName(), distance, false); 
	        adapter.addMessageLine(ml);
		}
		adapter.setReadMessages(read);
		setListAdapter(adapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		GIActivitySelector.startActivity(this, gis[position]);
	}
	
	protected void onResume() {
		super.onResume();
		doDatabaseQueryOnAuthenticated();
		registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	public boolean isGenItemActivity() {
		return false;
	}
	
	@Override
	public boolean isMessage() {
		return false;
	}

}
