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
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.RunListRecord;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.LocationService;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListRunsParticipateActivity extends GeneralActivity implements ListitemClickInterface {
	private Run[] runs = null;
	private GenericMessageListAdapter adapter;
	private boolean unregisterStatus = false;
	private View footerView;
	

	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("runs", runs);
		outState.putBoolean("unregisterStatus", unregisterStatus);
	}

	protected void unpackBundle(Bundle inState) {
		super.unpackBundle(inState);
		unregisterStatus = inState.getBoolean("unregisterStatus");
		runs = (Run[]) inState.getSerializable("runs");
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			setContentView(R.layout.listexcursionscreen);
			
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		RunDelegator.getInstance().synchronizeRunsWithServer(this);
		GameDelegator.getInstance().synchronizeParticipateGamesWithServer(this);
		renderExcursionList();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MenuHandler.SCAN_RUN, 0, getString(R.string.scanRun));
		menu.add(0, MenuHandler.UNREGISTER, 1, getString(R.string.delete)); // getString(R.string.scanTagMenu)
		return true;
	}

	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			checkAuthentication();
			renderExcursionList();
		}
	}

	private void checkAuthentication() {
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		}
	}

	private void renderExcursionList() {
		final ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
		runs = RunDelegator.getInstance().getRuns();
		if (runs != null) {
			for (int i = 0; i < runs.length; i++) {
				RunListRecord r = new RunListRecord(runs[i]);
				if (unregisterStatus)
					r.setShowCheckBox(true);
				runsList.add(r);
			}
		}
		ListView listView = (ListView) findViewById(R.id.listRuns);
		if (footerView != null)
			listView.removeFooterView(footerView);

		if (unregisterStatus) {
			footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
			
			listView.addFooterView(footerView);
			Button unregisterButton = (Button) findViewById(R.id.unregisterButton);
			unregisterButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (GenericListRecord r: runsList) {
						if (r.isChecked()) {
							RunDelegator.getInstance().unregisterRun(ListRunsParticipateActivity.this, r.getId());
						}
					}
					unregisterStatus = false;
					renderExcursionList();
				}
			});
			
			Button unregisterCancelButton = (Button) findViewById(R.id.unregisterCancelButton);
			unregisterCancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unregisterStatus = false;
					renderExcursionList();
				}
			});	
		}

		if (adapter == null || !adapter.isEqual(runsList)) {
			adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsList);
			adapter.setOnListItemClickCallback(this);
			listView.setAdapter(adapter);
		}

	}

	@Override
	public boolean setOnLongClickListener(View v, int position, final GenericListRecord messageListRecord) {
		if (unregisterStatus) return false;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.cacheGame)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				RunListRecord rlr = (RunListRecord) messageListRecord;
				GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(ListRunsParticipateActivity.this, rlr.getId(), rlr.getGameId());
			}
		}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}

	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		if (unregisterStatus) return;
		long id = position;
		PropertiesAdapter pa = new PropertiesAdapter(this);
		Long runId = runs[(int) id].getRunId();
		pa.setCurrentRunId(runId);

		if (pa.getRunStart(id) == 0)
			pa.setRunStart(id, System.currentTimeMillis());
		if (pa.getCurrentRunId() != -1) {
			Intent i = null;
			Game g = GameCache.getInstance().getGame(runs[(int) id].getGameId());
			boolean mapView = true;
			if (g != null && g.getConfig() != null) {
				mapView = g.getConfig().getMapAvailable();
				if (g.getConfig().getLocationUpdates() != null && !g.getConfig().getLocationUpdates().isEmpty()) {
					Intent intent = new Intent(this, LocationService.class);
					intent.putExtra("bean", g.getConfig());
					startService(intent);
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

				startActivity(i);
				ActionsDelegator.getInstance().publishStartRunActon(this, runId, pa.getUsername());
			}
		}
	}

	public boolean isGenItemActivity() {
		return false;
	}

	public boolean showStatusLed() {
		return true;
	}

	protected void newNfcAction(final String action) {
		
		Intent i = new Intent(this, ListOpenRunsActivity.class);
		i.putExtra(ListOpenRunsActivity.TAG_ID, action);
		startActivity(i);

	}

	public void showUnregister() {
		if (!NetworkSwitcher.isOnline(ListRunsParticipateActivity.this)) {
			Toast.makeText(ListRunsParticipateActivity.this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			return;
		}
		unregisterStatus = true;
		renderExcursionList();

	}

}
