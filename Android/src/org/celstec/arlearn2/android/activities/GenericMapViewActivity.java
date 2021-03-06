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

import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.GeneralItemReceiver;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class GenericMapViewActivity extends Activity implements ARLearnBroadcastReceiver {

	private GenericBroadcastReceiver broadcastReceiver;
	protected MenuHandler menuHandler;

	protected double lat = -1;
	protected double lng = -1;
	protected GeneralItem[] gis = new GeneralItem[0];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		broadcastReceiver = new GenericBroadcastReceiver(this);
		initListMapButton();
		
		Intent gimIntent = new Intent();
		gimIntent.setAction(GeneralItemReceiver.action);
		sendBroadcast(gimIntent);
		
		ActionsDelegator.getInstance().synchronizeActionsWithServer(this);

	}
	
	protected void onResume() {
		super.onResume();
		menuHandler = new MenuHandler(this);
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		}
		broadcastReceiver.onResume();
		retrieveItemsFromDb();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (broadcastReceiver != null)
			broadcastReceiver.onPause();
	}
	
	public void animateToMyLocation() {
		
	}
	
	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		if (render) makeGeneralItemVisible();
		
	}

	@Override
	public boolean showStatusLed() {
		return true;
	}

	private void retrieveItemsFromDb() {
		TreeSet<GeneralItem> gil = GeneralItemVisibilityCache.getInstance().getAllVisibleLocations(this.getRunId());
		if (gil != null) {
			gis = gil.toArray(new GeneralItem[] {});
		} 
		
//		DBAdapter db = new DBAdapter(this);
//		db.openForRead();

		//gis = (GeneralItem[]) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).queryWithLocation(this.getRunId());
		//TODO query with location
//		db.close();
	}
	
	protected void makeGeneralItemVisible() {
		retrieveItemsFromDb();
		Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
		if (runId == null || RunCache.getInstance().getRun(runId) == null) {
			this.finish();
		}
//		itemsOverlay.syncItems(this);
//		responsesOverlay.syncItems(this);
//		mv.invalidate();
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
	
	public long getRunId() {
		PropertiesAdapter pa = new PropertiesAdapter(this);
		return pa.getCurrentRunId();
	}
	
	public void initListMapButton() {
		ImageView tv = (ImageView) findViewById(R.id.mapViewId);
		if (tv == null) {
			return;
		}
		tv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(GenericMapViewActivity.this, ListMapItemsActivity.class);
				i.putExtra("runId", GenericMapViewActivity.this.getRunId());
				startActivity(i);
			}
		});
	}
	
}
