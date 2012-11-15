package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.GameReceiver;
import org.celstec.arlearn2.android.broadcast.GeneralItemReceiver;
import org.celstec.arlearn2.android.broadcast.RunReceiver;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;

import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.RunListRecord;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.service.LocationService;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.RunClient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ListExcursionsActivity extends GeneralActivity implements ListitemClickInterface {
	private Run[] runs = null;
//	private HashMap<Long, Game> games;
	private GenericMessageListAdapter adapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			setContentView(R.layout.listexcursionscreen);
			syncRuns();
//			Intent runSyncIntent = new Intent();
//			runSyncIntent.setAction(RunReceiver.action);
//			sendBroadcast(runSyncIntent);

		}
	}
	
	private void syncRuns() {
		Intent runIntent = new Intent();
		runIntent.setAction(RunReceiver.action);
		sendBroadcast(runIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initFromDb();
		renderExcursionList();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			checkAuthentication();
			initFromDb();
			renderExcursionList();
		}
	}

	private void checkAuthentication() {
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		}
	}

	private void renderExcursionList() {
		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
		runs = RunCache.getInstance().getRuns();
		if (runs != null) {

			for (int i = 0; i < runs.length; i++) {
				RunListRecord r = new RunListRecord(runs[i]);
				runsList.add(r);
			}
		}
		ListView listView = (ListView) findViewById(R.id.listRuns);

		adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsList);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean setOnLongClickListener(View v, int position, final GenericListRecord messageListRecord) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("cache this game? - i18").setPositiveButton("yes - i18", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				RunListRecord rlr = (RunListRecord) messageListRecord;
				if (rlr.getGameId() != null) {
					Intent runIntent = new Intent();
					runIntent.setAction(GameReceiver.action);
					runIntent.putExtra(GameReceiver.GAME_ID, rlr.getGameId());
					sendBroadcast(runIntent);
				}
			}
		}).setNegativeButton("no - i18", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}
	
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		long id = position;
		PropertiesAdapter pa = new PropertiesAdapter(this);
		pa.setCurrentRunId(runs[(int) id].getRunId());

		if (pa.getRunStart(id) == 0)
			pa.setRunStart(id, System.currentTimeMillis());
		if (pa.getCurrentRunId() != -1) {
			Intent i = null;
//			DBAdapter db = new DBAdapter(this);
//			db.openForRead();
			// Game g = (Game)
			// db.getGameAdapter().queryById(runs[(int)id].getGameId());
			Game g = GameCache.getInstance().getGame(runs[(int) id].getGameId());
//			Game g = games.get(runs[(int) id].getGameId());
			// result = (Run[])
			// ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).query();
//			db.close();
			boolean mapView = true;
			if (g != null && g.getConfig() != null) {
				mapView = g.getConfig().getMapAvailable();
				if (g.getConfig().getLocationUpdates() != null && !g.getConfig().getLocationUpdates().isEmpty()) {
					Intent intent = new Intent(this, LocationService.class);
					intent.putExtra("bean", g.getConfig());
					startService(intent);
				}
			}
			if (mapView) {
				int view = Config.MAP_TYPE_GOOGLE_MAPS;
				if (g.getConfig() != null && g.getConfig().getMapType() != null) {
					view = g.getConfig().getMapType();
				}
				switch (view) {
				case Config.MAP_TYPE_GOOGLE_MAPS:
					i = new Intent(this, MapViewActivity.class);
					break;
				case Config.MAP_TYPE_OSM:
					i = new Intent(this, OsmMapViewActivity.class);
				default:
					break;
				}
				
			} else {
				i = new Intent(this, ListMessagesActivity.class);
			}

			// i.putExtra("runId", id);
			startActivity(i);
			ActionDispatcher.startRun(ListExcursionsActivity.this);

			// Intent gimIntent = new Intent();
			// gimIntent.setAction(GeneralItemReceiver.action);
			// sendBroadcast(gimIntent);

			// CheckGameConfig task = new CheckGameConfig(this); //TODO remove
			// this
			// task.execute(new Object[] {});
		}

	}

	public void initFromDb() {
		runs = RunCache.getInstance().getRuns();
		if (runs == null) {
			return;
		}
	}

	public boolean isGenItemActivity() {
		return false;
	}

	public boolean showStatusLed() {
		return true;
	}

	protected void newNfcAction(final String action) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListExcursionsActivity.this);
		builder.setMessage(getString(R.string.registerRun)).setCancelable(false);
		
		builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				new Thread(new Runnable() {
					public void run() {
						try {
							RunClient.getRunClient().selfRegister(getPropertiesAdapter().getFusionAuthToken(), action);
						} catch (Exception ex) {
							Log.e("exception", ex.getMessage(), ex);
						}
					}
				}).start();
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	
}
