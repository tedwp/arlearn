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
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;
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
	
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) 
			renderList();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listexcursionscreen);

		RunDelegator.getInstance().loadRun(this, getPropertiesAdapter().getCurrentRunId());
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
		Long runId = menuHandler.getPropertiesAdapter().getCurrentRunId();
		if (runId == null || RunCache.getInstance().getRun(runId) == null) {
			this.finish();
		}
		
		TreeSet<GeneralItem> gil = GeneralItemVisibilityCache.getInstance().getAllVisibleItems(runId);
		if (gil == null) {
			return;
		} 
		gis = gil.toArray(new GeneralItem[] {});
		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listRuns); 

		adapter = new GenericMessageListAdapter(this,R.layout.listexcursionscreen, runsList);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);
		
		for (int j = 0; j < gis.length; j++) {
			String distance = "";
			if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
				distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
			}
			MessageListRecord r = new MessageListRecord(gis[j], runId, this);
			r.setDistance(distance);
			adapter.add(r);
		}
	}

	//TODO move to other place
	public static int getIcon(GeneralItem gi){
		if (gi.getType().equals(MultipleChoiceTest.class.getName())) {
			return R.drawable.question;
		} else
		if (gi.getType().equals(YoutubeObject.class.getName())) {
			return R.drawable.youtube;
		} else
			if (gi.getType().equals(NarratorItem.class.getName())) {
				if (((NarratorItem) gi).getOpenQuestion() == null) {
					return R.drawable.info;	
				} else {
					return R.drawable.question;
				}
				
			}
//		else 	if (gi.getType().equals(OpenQuestion.class.getSimpleName())) {
//			return R.drawable.speechbubble_green;
//		}
		else 	if (gi.getType().equals(AudioObject.class.getName())) {
			return R.drawable.speechbubble_blue;
		}
		return 0;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
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
	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}
	public boolean showStatusLed() {
		return true;
	}

}
