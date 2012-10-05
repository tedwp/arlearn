package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
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
        		System.out.println("longPress "+index);

				return false;
			}

			@Override
			public boolean onItemSingleTapUp(int index, OverlayItem arg1) {
				// TODO Auto-generated method stub
        		System.out.println("onItemSingleTapUp "+index);
        		GIActivitySelector.startActivity(OsmMapViewActivity.this, gis[index]);
				return false;
			}
		};
		itemsOverlay = new OsmGeneralItemizedIconOverlay2(this, new ArrayList<OverlayItem>(), gestureListener);
		mv.getOverlays().add(itemsOverlay);

	}
	protected void onResume() {
		super.onResume();
		myLocation.enableMyLocation();
		itemsOverlay.setGeneralItemList(gis);
		mv.invalidate();
		Location loc = LocationService.getBestLocation(this);
		lat = loc.getLatitude();
		lng = loc.getLongitude();
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
