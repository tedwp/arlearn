package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.MessageListRecord;
import org.celstec.arlearn2.android.service.LocationService;
import org.celstec.arlearn2.android.util.GPSUtil;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListMapItemsActivity extends GeneralActivity implements ListitemClickInterface{

	private GenericMessageListAdapter adapter;
	private GeneralItem[] gis = new GeneralItem[0]; 
	private long[] read = new long[0]; 

	private double lng;
	private double lat;
	private long runId;
	
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) 
			renderList();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);
	}
	
	private void renderList(){
		Location loc = LocationService.getBestLocation(this);
		if (loc != null) {
			setLat(loc.getLatitude());
			setLng(loc.getLongitude());
		} else {
			setLat(50.878);
			setLng(5.96);
		}
		

//		setContentView(R.layout.list_map_items); TODO delete list_map_items
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		gis = (GeneralItem[]) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).query(this.getRunId(), GeneralItemAdapter.VISIBLE);
		read = (long[]) ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).queryReadItems(this.getRunId());
		
		

		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();

		ListView listView = (ListView) findViewById(R.id.listRuns); 

		adapter = new GenericMessageListAdapter(this,R.layout.listexcursionscreen, runsList);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);
		
		MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
		for (int j = 0; j < gis.length; j++) {
			String distance = "";
			if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
				distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
			}
			MessageListRecord r = new MessageListRecord(gis[j], read,mc);
			r.setDistance(distance);
			adapter.add(r);
		}
		db.close();
		
//		adapter.emptyList();
//		for (int j = 0; j < gis.length; j++) {
//			String distance = "";
//			if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
//				distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
//			}
//			MessageListAdapter.MessageLine ml = (adapter).new MessageLine(gis[j].getId(), gis[j].getName(), distance, false); 
//	        adapter.addMessageLine(ml);
//		}
//		adapter.setReadMessages(read);
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
	
	public long getRunId(){
		return runId;
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
			renderList();
		}
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public boolean isGenItemActivity() {
		return false;
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		GIActivitySelector.startActivity(this, gis[position]);
		
	}
	
	public boolean showStatusLed() {
		return true;
	}

}
