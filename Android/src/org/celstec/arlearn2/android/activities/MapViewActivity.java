package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.notificationbeans.GeneralItem;
import org.celstec.arlearn2.android.db.notificationbeans.LocationUpdate;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.celstec.arlearn2.android.db.notificationbeans.UpdateScore;
import org.celstec.arlearn2.android.maps.GenericItemsOverlay;
import org.celstec.arlearn2.android.maps.ResponsesOverlay;
import org.celstec.arlearn2.android.maps.UsersOverlay;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.score.ScoreHandler;
import org.celstec.arlearn2.android.service.NotificationService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater.Factory;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MapViewActivity extends MapActivity {

	private MapView mv;
	private MyLocationOverlay myLocation;
	private MapController control;
	private GenericItemsOverlay itemsOverlay;
	private ResponsesOverlay responsesOverlay;
	private UsersOverlay usersOverlay;

	private double lat = -1;
	private double lng = -1;
//	private long runId;
	
	private Handler mHandler = new Handler() ;
	private ScoreHandler scoreHandler = new ScoreHandler(this);
	protected MenuHandler menuHandler; 
	
	private Intent intent;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			NotificationBean bean = (NotificationBean) intent.getExtras().getSerializable("bean");
			if (bean.getClass().equals(UpdateScore.class) && menuHandler.getPropertiesAdapter().isScoringEnabled()) {
				scoreHandler.setScore((UpdateScore) bean);
			}
			if (bean.getClass().equals(LocationUpdate.class)) {
				usersOverlay.updateLocation((LocationUpdate) bean);
				
				mv.invalidate();
			}
			if (bean.getClass().equals(GeneralItem.class)) {
				GeneralItem gibean = (GeneralItem) bean;
				if ("visible".equals(gibean.getAction())) makeGeneralItemVisible(gibean);
			}
		}
	};
	
	protected boolean isRouteDisplayed() {
		return false;
	}
	long lastTap = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(this, NotificationService.class);
		
//		runId = getIntent().getLongExtra("runId", 0);
//		runId = intent.getExtras().getLong("runId");
		setContentView(R.layout.map_view);
		
		mv = (MapView) findViewById(R.id.map);
		mv.setBuiltInZoomControls(true);
		myLocation = new MyLocationOverlay(this, mv) {
			public synchronized void onLocationChanged(Location location) {
				super.onLocationChanged(location);
					lat = (double) (location.getLatitude() * 1E6);
					lng = (double) (location.getLongitude() * 1E6);
			}

			
			@Override
			public boolean onTap(final GeoPoint p, MapView map) {
				long time = System.currentTimeMillis();
				if ((time - lastTap) < 1000) {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(MapViewActivity.this);
					builder.setMessage(getString(R.string.makeAnnotation))
					       .setCancelable(false)
					       .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	Intent intent = new Intent(MapViewActivity.this, AnnotateActivity.class);
					   			intent.putExtra("runId", menuHandler.getPropertiesAdapter().getCurrentRunId());
					   			intent.putExtra("lat",  ((double)p.getLatitudeE6() / 1E6));
					   			intent.putExtra("lng",   ((double)p.getLongitudeE6() / 1E6));

					   			startActivity(intent);
					           }
					       })
					       .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
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
		mHandler.postDelayed(checkForUpdates, 10000);
		initListMapButton();
		
	}
	
	public void animateToMyLocation() {
		control.animateTo(new GeoPoint((int) lat, (int) (lng)));
		mv.invalidate();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuHandler.onOptionsItemSelected(item);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater=getMenuInflater();
//		
//	    inflater.inflate(R.menu.map_menu, menu);
//	    setMenuBackground(); 
			if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
				menu.add(0, MenuHandler.MESSAGES, 0, getString(R.string.messagesMenu));
				menu.add(0, MenuHandler.MY_LOCATION, 0, getString(R.string.myLocationMenu));
			} else {
				menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login));
			}
			menu.add(0, MenuHandler.EXIT, 0, getString(R.string.exit));
		return true;
	}
	
	
//	protected void setMenuBackground() {
//		// Log.d(TAG, "Enterting setMenuBackGround");
//
//		getLayoutInflater().setFactory(new Factory() {
//			public View onCreateView(String name, Context context, AttributeSet attrs) {
//				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
//					 if (name.equalsIgnoreCase
//		                      ("com.android.internal.view.menu.IconMenuItemView")) {
//					try { // Ask our inflater to create the view
//						LayoutInflater f = getLayoutInflater();
//						final View view = f.createView(name, null, attrs);
//						/*
//						 * The background gets refreshed each time a new item is
//						 * added the options menu. So each time Android applies
//						 * the default background we need to set our own
//						 * background. This is done using a thread giving the
//						 * background change as runnable object
//						 */
//						new Handler().post(new Runnable() {
//							public void run() {
//								// sets the background color
//								view.setBackgroundResource(R.drawable.menu_selector);
////								// sets the text color
////								((TextView) view).setTextColor(Color.BLACK);
////								// sets the text size
////								((TextView) view).setTextSize(18);
//							}
//						});
//						return view;
//					} catch (Exception e) {
//						Log.e("##Menu##", 
//	                              "Could not create a custom view for menu: "
//	                                                      + e.getMessage(), e);
//					}
//				}
//				}
//				return null;
//			};
//		});
//	}
	
	    
	
	protected void onResume() {
		super.onResume();
		menuHandler = new MenuHandler(this);
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		}
		myLocation.enableMyLocation();
		mHandler.removeCallbacks(checkForUpdates);
		mHandler.post(checkForUpdates);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
		if (menuHandler.getPropertiesAdapter().isScoringEnabled() && menuHandler.getPropertiesAdapter().getTotalScore() != null && menuHandler.getPropertiesAdapter().getTotalScore() != Long.MIN_VALUE) {
			scoreHandler.setScore((int) menuHandler.getPropertiesAdapter().getTotalScore().longValue());
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocation.disableMyLocation();
		mHandler.removeCallbacks(checkForUpdates);
		unregisterReceiver(broadcastReceiver);
	}
	
	
	
	private Runnable checkForUpdates = new Runnable() {
		
		public void run() {
			itemsOverlay.syncItems(MapViewActivity.this);
			responsesOverlay.syncItems(MapViewActivity.this);
			mHandler.postDelayed(checkForUpdates, 120000);
		}
	};
	
	private void makeGeneralItemVisible(GeneralItem gi) {
		if (gi.getLat() ==null && gi.getLng() == null) {
		} else {
			itemsOverlay.syncItems(this);
			responsesOverlay.syncItems(this);
			mv.invalidate();
		}
	}
	
	public long getRunId(){
		PropertiesAdapter pa = new PropertiesAdapter(this);
		return pa.getCurrentRunId();
//		return runId;
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
	
	
}
