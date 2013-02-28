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
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.maps.OsmGeneralItemizedIconOverlay2;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.LocationService;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import com.google.android.maps.GeoPoint;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;

public class OsmMapViewActivity extends GenericMapViewActivity {

	private MyLocationOverlay myLocation;

	private MapView mv;
	private MapController control;
	private OsmGeneralItemizedIconOverlay2 itemsOverlay;
	
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.map_view_osm);
		super.onCreate(savedInstanceState);
		

		mv = (MapView) findViewById(R.id.map);
		mv.setTileSource(TileSourceFactory.MAPNIK);
		mv.setClickable(true);
		mv.setBuiltInZoomControls(true);

		mv.getController().setZoom(11);
//		mv.setBuiltInZoomControls(false);
		control = mv.getController();
		myLocation = new MyLocationOverlay(this, mv) {
			public synchronized void onLocationChanged(Location location) {
				super.onLocationChanged(location);
				lat = location.getLatitude();
				lng = location.getLongitude();
			}
		};
		
		Drawable defaultMarker = OsmMapViewActivity.this.getResources().getDrawable(R.drawable.speechbubble_blue);
	
		mv.getOverlays().add(myLocation);
		OnItemGestureListener<OverlayItem> gestureListener = new OnItemGestureListener<OverlayItem>() {

			@Override
			public boolean onItemLongPress(int index, OverlayItem arg1) {
				return false;
			}

			@Override
			public boolean onItemSingleTapUp(int index, OverlayItem arg1) {
        		GIActivitySelector.startActivity(OsmMapViewActivity.this, gis[index]);
				return false;
			}
		};
		itemsOverlay = new OsmGeneralItemizedIconOverlay2(this, new ArrayList<OverlayItem>(), gestureListener);
		mv.getOverlays().add(itemsOverlay);

	}
	protected void onResume() {
		super.onResume();
		Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
		if (runId == null || RunCache.getInstance().getRun(runId) == null) {
			this.finish();
		}
		myLocation.enableMyLocation();
		itemsOverlay.setGeneralItemList(gis);
		mv.invalidate();
		Location loc = LocationService.getBestLocation(this);
		if (loc != null) {
			lat = loc.getLatitude();
			lng = loc.getLongitude();	
		} else {
			lat = 50.877802d;
			lng = 5.957238d;
		}
		
		animateToMyLocation();

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocation.disableMyLocation();
	}

	public void animateToMyLocation() {
		control.animateTo(lat, lng);
		mv.invalidate();
	}

	protected void makeGeneralItemVisible() {
		super.makeGeneralItemVisible();
		itemsOverlay.setGeneralItemList(gis);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.DOWNLOAD_MAP_TILES, 0, getString(R.string.downloadMap)); 
		} 
		return super.onCreateOptionsMenu(menu);
	}
	
	
}
