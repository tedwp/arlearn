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
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.generalItem.*;
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

    private TreeSet<String> sections;
    private HashMap<String, Boolean> showSection = new HashMap<String, Boolean>();

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

    private void loadSections(TreeSet<GeneralItem> gil) {
        sections = new TreeSet<String>();
        for (GeneralItem gi: gil) {
            if (gi.getSection() != null) {
                sections.add(gi.getSection());
                if (!showSection.containsKey(gi.getSection())) showSection.put(gi.getSection(), true);
            }
        }
    }
    private boolean hasSections() {
        if (sections.size() == 1 && sections.first().equals("")) return false;
        return true;
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

        if (gil != null) {
            loadSections(gil);
            if (hasSections()) {
                loadMessagesWithSections(gil);
            }   else {
                loadMessagesNoSections(gil);
            }

            renderMessagesList();
        }

//		gis = gil.toArray(new GeneralItem[] {});
//		ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
//		ListView listView = (ListView) findViewById(R.id.listRuns);
//
//		adapter = new GenericMessageListAdapter(this,R.layout.listexcursionscreen, runsList);
//		adapter.setOnListItemClickCallback(this);
//		listView.setAdapter(adapter);
//
//		for (int j = 0; j < gis.length; j++) {
//			String distance = "";
//			if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
//				distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
//			}
//			MessageListRecord r = new MessageListRecord(gis[j], runId, this);
//			r.setDistance(distance);
//			adapter.add(r);
//		}
	}

    private void loadMessagesNoSections(TreeSet<GeneralItem> gil) {
        gis = new GeneralItem[gil.size()];
        int i = 0;
        for (Iterator<GeneralItem> iterator = gil.iterator(); iterator.hasNext();) {
            gis[i++] = iterator.next();;
        }
    }

    private void loadMessagesWithSections(TreeSet<GeneralItem> gil) {
        ArrayList<GeneralItem> gisList = new ArrayList<GeneralItem>(gil.size()+sections.size());
        gis = new GeneralItem[gil.size()+sections.size()];
        int i = 0;

        for (String section: sections) {
            GeneralItem sectionItem = new GeneralItem();
            sectionItem.setName(section);
            sectionItem.setGameId(getGameId());
            sectionItem.setIconUrl(""+showSection.get(section));
            gisList.add(sectionItem);

            for (Iterator<GeneralItem> iterator = gil.iterator(); iterator.hasNext();) {

                GeneralItem item = iterator.next();
                if (section.equals(item.getSection())&&showSection.get(section)) {
                    gisList.add(item);
                }
            }

        }
        gis = gisList.toArray(new GeneralItem[]{});
    }

    private void renderMessagesList() {
        ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
        Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();

        ListView listView = (ListView) findViewById(R.id.listRuns);
        adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsList);
        adapter.setOnListItemClickCallback(this);
        listView.setAdapter(adapter);

        for (int j = 0; j < gis.length; j++) {

            MessageListRecord r = new MessageListRecord(gis[j], runId, this);
            String distance = "";
            if (!( gis[j].getLng() == null && gis[j].getLat() == null)) {
                distance =distanceToString(GPSUtil.distance(gis[j].getLat(), gis[j].getLng(), lat, lng, GPSUtil.METERS));
            }
            r.setDistance(distance);

            adapter.add(r);
        }
    }

//    public static void setIcon(ImageView iv, GeneralItem gi) {
//        if (gi.getIconUrl()!= null) {
//
//        }
//    }

    public static Drawable getIconAsDrawable(Context ctx,
                                             GeneralItem gi){
        Drawable returnDrawable = null;
        if (gi.getIconUrl() != null) {
            Uri localAudioUri = GeneralItemsDelegator.getInstance().getLocalMediaUriMap(gi).get(GeneralItemsDelegator.ICON_LOCAL_ID);
            if (localAudioUri!= null) {
                returnDrawable = Drawable.createFromPath(localAudioUri.getPath());
//                return Drawable.createFromPath(localAudioUri.getPath());
            }
        }
        if (returnDrawable == null) {
            int icon = getIcon(gi);
            if (icon == 0) return null;
            returnDrawable = ctx.getResources().getDrawable(getIcon(gi));
//            return ctx.getResources().getDrawable(getIcon(gi));
        }
        if (returnDrawable != null) {
            final float scale = ctx.getResources().getDisplayMetrics().density;
//        Rect bounds = icon.getBounds();
//            Rect newBounds = new Rect(0,0, (int) (48 * scale + 0.5f), (int) (48 * scale + 0.5f));
//            returnDrawable.setBounds(newBounds);
            Bitmap bitmap = ((BitmapDrawable) returnDrawable).getBitmap();

            Drawable d = new BitmapDrawable(ctx.getResources(), Bitmap.createScaledBitmap(bitmap, (int) (48 * scale + 0.5f), (int) (48 * scale + 0.5f), true));
            return d;
        }
        return returnDrawable;

    }
	//TODO move to other place
	public static int getIcon(GeneralItem gi){
		if (gi.getType().equals(MultipleChoiceTest.class.getName())) {
			return R.drawable.question;
		} else if (gi.getType().equals(SingleChoiceTest.class.getName())) {
            return R.drawable.question;
        } else if (gi.getType().equals(MultipleChoiceImageTest.class.getName())) {
            return R.drawable.question;
        } else if (gi.getType().equals(SingleChoiceImageTest.class.getName())) {
            return R.drawable.question;
        } else if (gi.getType().equals(VideoObject.class.getName())) {
            return R.drawable.video_48x48;
        } else if (gi.getType().equals(YoutubeObject.class.getName())) {
			return R.drawable.youtube;
		} else
			if (gi.getType().equals(NarratorItem.class.getName())) {
				if (((NarratorItem) gi).getOpenQuestion() == null) {
					return R.drawable.gi_narrator;
				} else {
					return R.drawable.question;
				}
				
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

        if (gis[position].getId() != null) {
            GIActivitySelector.startActivity(this, gis[position]);
        } else {
            String section = gis[position].getName();
            showSection.put(section, !showSection.get(section   ));
            renderList();
        }
		
	}
	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}
	public boolean showStatusLed() {
		return true;
	}

	@Override
	public GeneralItem getGeneralItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		// TODO Auto-generated method stub
		
	}

}
