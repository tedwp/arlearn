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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.*;
import org.celstec.arlearn2.android.maps.GenericItemsOverlay;
import org.celstec.arlearn2.android.maps.ResponsesOverlay;
import org.celstec.arlearn2.android.maps.UsersOverlay;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.score.ScoreHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import org.celstec.arlearn2.android.variable.VariableDisplayHandler;

public class MapViewActivity extends MapActivity implements ARLearnBroadcastReceiver {

	private MapView mv;
	private MyLocationOverlay myLocation;
	private MapController control;
	private GenericItemsOverlay itemsOverlay;
	private ResponsesOverlay responsesOverlay;
	private UsersOverlay usersOverlay;

	private double lat = -1;
	private double lng = -1;

    private VariableDisplayHandler variableDisplayHandler = new VariableDisplayHandler(this);
	protected MenuHandler menuHandler;

	private GenericBroadcastReceiver broadcastReceiver;

	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		if (render) {
			makeGeneralItemVisible();
           if (variableDisplayHandler != null) variableDisplayHandler.sync();
        }
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	long lastTap = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);

		broadcastReceiver = new GenericBroadcastReceiver(this);

		// Intent gimIntent = new Intent();
		// gimIntent.setAction(GeneralItemReceiver.action);
		// sendBroadcast(gimIntent);

		mv = (MapView) findViewById(R.id.map);
		mv.setBuiltInZoomControls(true);
		myLocation = new MyLocationOverlay(this, mv) {
			public synchronized void onLocationChanged(Location location) {
				super.onLocationChanged(location);
				lat = (location.getLatitude() * 1E6);
				lng = (location.getLongitude() * 1E6);
			}

			@Override
			public boolean onTap(final GeoPoint p, MapView map) {
				long time = System.currentTimeMillis();
				if ((time - lastTap) < 1000) {

					AlertDialog.Builder builder = new AlertDialog.Builder(MapViewActivity.this);
					builder.setMessage(getString(R.string.makeAnnotation)).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent(MapViewActivity.this, AnnotateActivity.class);
							intent.putExtra("runId", menuHandler.getPropertiesAdapter().getCurrentRunId());
							intent.putExtra("lat", ((double) p.getLatitudeE6() / 1E6));
							intent.putExtra("lng", ((double) p.getLongitudeE6() / 1E6));

							startActivity(intent);
						}
					}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
				lastTap = time;

				return super.onTap(p, map);
			}

		};
		mv.getOverlays().add(myLocation);
		Drawable defaultMarker = MapViewActivity.this.getResources().getDrawable(R.drawable.speechbubble_blue);
		Drawable userMarker = MapViewActivity.this.getResources().getDrawable(R.drawable.user);
		itemsOverlay = new GenericItemsOverlay(defaultMarker, MapViewActivity.this);
		responsesOverlay = new ResponsesOverlay(defaultMarker, MapViewActivity.this);
		usersOverlay = new UsersOverlay(userMarker);
		mv.getOverlays().add(itemsOverlay);
		mv.getOverlays().add(responsesOverlay);
		mv.getOverlays().add(usersOverlay);
		control = mv.getController();
		// mHandler.postDelayed(checkForUpdates, 10000);

		initListMapButton();

		ActionsDelegator.getInstance().synchronizeActionsWithServer(this);
		long runId = PropertiesAdapter.getInstance(this).getCurrentRunId();

		RunDelegator.getInstance().loadRun(this, runId);
		ResponseDelegator.getInstance().synchronizeResponsesWithServer(this, runId);

	}

	public void animateToMyLocation() {
		control.animateTo(new GeoPoint((int) lat, (int) (lng)));
		mv.invalidate();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return menuHandler.onOptionsItemSelected(item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.MESSAGES, 0, getString(R.string.messagesMenu));
			menu.add(0, MenuHandler.MY_LOCATION, 0, getString(R.string.myLocationMenu));
		} else {
			menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login));
		}
		menu.add(0, MenuHandler.SYNC, 0, getString(R.string.sync));
		return true;
	}

	protected void onResume() {
		super.onResume();
		Long runId = PropertiesAdapter.getInstance(this).getCurrentRunId();
		if (runId == -1 || RunCache.getInstance().getRun(runId) == null) {
			this.finish();
		}
		Long gameId = RunCache.getInstance().getGameId(runId);
		if (gameId == null) {
			finish();
		} else {
			GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, runId, gameId);

			menuHandler = new MenuHandler(this);
			if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
				this.finish();
			}

			myLocation.enableMyLocation();

			if (broadcastReceiver != null)
				broadcastReceiver.onResume();
            variableDisplayHandler.setGameId(gameId);
            variableDisplayHandler.setRunId(getRunId());
            variableDisplayHandler.sync();
//            VariableDelegator.getInstance().syncVariable(this, getRunId(), gameId, "score");
//			if (VariableDelegator.getInstance().varExists(getRunId(), "score")) {
////                if (menuHandler.getPropertiesAdapter().isScoringEnabled() && menuHandler.getPropertiesAdapter().getTotalScore() != null && menuHandler.getPropertiesAdapter().getTotalScore() != Long.MIN_VALUE) {
////				scoreHandler.setScore((int) menuHandler.getPropertiesAdapter().getTotalScore().longValue());
//                scoreHandler.setScore(VariableDelegator.getInstance().getVariable(getRunId(), "score"));
//			}
//            variableDisplayHandler.sync();
			makeGeneralItemVisible();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocation.disableMyLocation();
		// mHandler.removeCallbacks(checkForUpdates);
		if (broadcastReceiver != null)
			broadcastReceiver.onPause();
	}

	// private Runnable checkForUpdates = new Runnable() {
	//
	// public void run() {
	// itemsOverlay.syncItems(MapViewActivity.this);
	// responsesOverlay.syncItems(MapViewActivity.this);
	// mHandler.postDelayed(checkForUpdates, 120000);
	// }
	// };

	private void makeGeneralItemVisible() {
		long runId = getRunId();
		itemsOverlay.syncItems(runId);
		responsesOverlay.syncItems(this, runId);
		mv.invalidate();
	}

	public long getRunId() {
		PropertiesAdapter pa = new PropertiesAdapter(this);
		return pa.getCurrentRunId();
	}

	public void initListMapButton() {
		ImageView tv = (ImageView) findViewById(R.id.mapViewId);
		tv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MapViewActivity.this, ListMapItemsActivity.class);
				i.putExtra("runId", MapViewActivity.this.getRunId());
				startActivity(i);
			}
		});
	}

	@Override
	public boolean showStatusLed() {
		return true;
	}

}
