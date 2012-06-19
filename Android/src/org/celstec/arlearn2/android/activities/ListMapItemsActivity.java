package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.db.notificationbeans.LocationUpdate;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.celstec.arlearn2.android.db.notificationbeans.UpdateScore;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.android.service.LocationService;
import org.celstec.arlearn2.android.util.GPSUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.MatrixCursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListMapItemsActivity extends GeneralListActivity {

	private MessageListAdapter adapter;
	private GeneralItem[] gis = new GeneralItem[0]; 
	private long[] read = new long[0]; 

	private double lng;
	private double lat;
	private long runId;

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//		public void onReceive(Context context, Intent intent) {
//			NotificationBean bean = (NotificationBean) intent.getExtras().getSerializable("bean");
//			if (bean.getClass().equals(org.celstec.arlearn2.android.db.notificationbeans.GeneralItem.class)) {
//				org.celstec.arlearn2.android.db.notificationbeans.GeneralItem gibean = (org.celstec.arlearn2.android.db.notificationbeans.GeneralItem) bean;
//				if ("visible".equals(gibean.getAction())) {
//					renderList();
//				}
//			}
//			
//
//		}
		
		public void onReceive(Context context, Intent intent) {
			Boolean forMe = intent.getExtras().getBoolean(ListMapItemsActivity.class.getCanonicalName(), false);
			if (forMe) {
				LedStatus.updateStatus(ListMapItemsActivity.this);
				renderList();
				
			}
		}
	};
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	
	
	
	private void renderList(){
		Location loc = LocationService.getBestLocation(this);
//		Location loc = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		Location loc2 = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		if (loc == null) {
//			loc = loc2;
//		} else {
//			if (loc2 != null) if (isBetterLocation(loc2, loc)) loc= loc2;
//			
//		}
//		
		
		if (loc != null) {
			setLat(loc.getLatitude());
			setLng(loc.getLongitude());
		} else {
			setLat(50.878);
			setLng(5.96);
		}

		setContentView(R.layout.list_map_items);
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		gis = (GeneralItem[]) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).query(this.getRunId(), GeneralItemAdapter.VISIBLE);
		read = (long[]) ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).queryReadItems(this.getRunId());
		db.close();
		

		adapter.emptyList();
		for (int j = 0; j < gis.length; j++) {
			String distance = "";
			if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
				distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
			}
			MessageListAdapter.MessageLine ml = (adapter).new MessageLine(gis[j].getId(), gis[j].getName(), distance, false); 
	        adapter.addMessageLine(ml);
		}
		adapter.setReadMessages(read);
	}

	//TODO move to other place
	public static int getIcon(GeneralItem gi){
		if (gi.getType().equals(MultipleChoiceTest.class.getName())) {
			return R.drawable.multiple_choice;
		} 
//		else 	if (gi.getType().equals(OpenQuestion.class.getSimpleName())) {
//			return R.drawable.speechbubble_green;
//		}
		else 	if (gi.getType().equals(AudioObject.class.getName())) {
			return R.drawable.audio_icon;
		}
		return 0;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

//	public void initMapButton() {
//		ImageView tv = (ImageView) findViewById(R.id.mapViewIcon);
//		tv.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(ListMapItemsActivity.this, MapViewActivity.class);
//				i.putExtra("runId", runId);
//				startActivity(i);
//			}
//		});
//	}
	
	public long getRunId(){
		return runId;
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		GIActivitySelector.startActivity(this, gis[position]);
	}
	
	public String distanceToString(double distance) {
		if (distance > 1) {
			return ((int) distance) + " m";
		} else {
			return "0 m";
		}
	}
	
	protected void onResume() {
		super.onResume();
		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			runId = getIntent().getLongExtra("runId", 0);
			adapter = new MessageListAdapter(this);
			setListAdapter(adapter);
			//TODO
//			registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
			registerReceiver(broadcastReceiver, new IntentFilter("org.celstec.arlearn.updateActivities"));

			renderList();
		}
		
		LedStatus.updateStatus(ListMapItemsActivity.this);

		
	}
	
	public boolean isGenItemActivity() {
		return false;
	}
	
	@Override
	public boolean isMessage() {
		return false;
	}

}
