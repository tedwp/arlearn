package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.CheckGameConfig;
import org.celstec.arlearn2.android.asynctasks.PublishActionTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;

import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.android.service.NotificationService;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.MatrixCursor;
import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListExcursionsActivity  extends GeneralListActivity {
	private Run[] runs = null;
	private ListAdapter adapter;
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			NotificationBean bean = (NotificationBean) intent.getExtras().getSerializable("bean");
			if (bean.getClass().equals(org.celstec.arlearn2.android.db.notificationbeans.Run.class)) {
				runs = getExcursionsFromDatabase();
				renderExcursionList();
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			setContentView(R.layout.listexcursionscreen);
			runs = getExcursionsFromDatabase();
			renderExcursionList();
		}
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		}
		menu.add(0, EXIT, 0, getString(R.string.exit));
//		menu.add(0, MenuHandler.RESET, 0, getString(R.string.reset));
		menu.add(0, MenuHandler.REFRESH, 0, getString(R.string.refresh));
		return true;
	}
	
	
	private void renderExcursionList() {
		final String[] matrix = { "_id", "title" };
		final String[] columns = { "title" };
		MatrixCursor mCursor = new MatrixCursor(matrix);
		if (runs != null) {
			for (int i = 0; i < runs.length; i++) {
//				mCursor.addRow(new String[] { ""+runs[i].getRunId(), runs[i].getTitle() });
				mCursor.addRow(new String[] { ""+i, runs[i].getTitle() });
			}
		}
		startManagingCursor(mCursor);
		final int[] layouts = {R.id.excursionItem1 };

		adapter = new SimpleCursorAdapter(this, R.layout.excursion_line, mCursor,
				columns, layouts);
		setListAdapter(adapter);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		PropertiesAdapter pa = new PropertiesAdapter(this);
		pa.setCurrentRunId(runs[(int)id].getRunId());
//		Intent intent = new Intent(this, BackgroundService.class);
//		startService(intent);
		
		if (pa.getRunStart(id) == 0) pa.setRunStart(id, System.currentTimeMillis());
		if (pa.getCurrentRunId() != -1) {
			Intent i = null; 
			DBAdapter db = new DBAdapter(this);
			db.openForRead();
			Game g = (Game) db.getGameAdapter().queryById(runs[(int)id].getGameId());
//			result = (Run[]) ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).query();
			db.close();
			boolean mapView = true;
			if (g != null && g.getConfig()!=null ) {
				mapView = g.getConfig().getMapAvailable();
			}
			if (mapView) {
				i = new Intent(this, MapViewActivity.class);
			} else {
				i = new Intent(this, ListMessagesActivity.class);
			}
			
//			i.putExtra("runId", id);
			startActivity(i);
			ActionDispatcher.startRun(ListExcursionsActivity.this);
			
			CheckGameConfig task = new CheckGameConfig(this); //TODO remove this
			task.execute(new Object[] {});
		}
	
	}

	protected void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}
	
	public Run[] getExcursionsFromDatabase() {
		Run[] result;
		DBAdapter db = new DBAdapter(this);
		db.openForWrite();
		result = (Run[]) ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).query();
		db.close();
		return result;
	}


	
	public boolean isGenItemActivity() {
		return false;
	}
	
	@Override
	public boolean isMessage() {
		return false;
	}
	
	
}
