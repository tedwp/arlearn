package org.celstec.arlearn2.android.db.beans;

import java.io.Serializable;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.Dependency;
import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.genItemActivities.VideoObjectActivity;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class GeneralItem implements Serializable {
	public static final String ID = "id";
	
	public boolean equals(GeneralItem other) {
		if (!nullSafeEquals(getId(),other.getId())) return false; 
		if (!nullSafeEquals(getName(),other.getName())) return false; 
		if (!nullSafeEquals(getDescription(),other.getDescription())) return false; 
		if (!nullSafeEquals(getDependsOn(),other.getDependsOn())) return false; 
		if (!nullSafeEquals(getType(),other.getType())) return false; 
		if (!nullSafeEquals(getRadius(),other.getRadius())) return false;
		if (!nullSafeEquals(getShowAtTimeStamp(),other.getShowAtTimeStamp())) return false; 
		if (!nullSafeEquals(getLng(),other.getLng())) return false; 
		if (!nullSafeEquals(getLat(),other.getLat())) return false;
		if (!nullSafeEquals(getRunId(),other.getRunId())) return false;
		
		if (!nullSafeEquals(getPayload(),other.getPayload())) return false; 
		return true;
	}
	private boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		return o1.equals(o2);
	}
	
	private String id;
	
	private int autoId;

	private String name;

	private String description;

	private String dependsOn;

	private String type;
	
	private Integer radius;
	
	private Long showAtTimeStamp;

	private Double lng;

	private Double lat;
	
	private String payload;
	
	private long runId;
	
	private boolean dependsOnVisible;
	
	private boolean timeVisible;

	
	public GeneralItem() {
		
	}
	public GeneralItem(GeneralItem gi) {
		setId(gi.getId());
		setName(gi.getName());
		setDescription(gi.getDescription());
		setDependsOn(gi.getDependsOn());
		setType(gi.getType());
		setRadius(gi.getRadius());
		setShowAtTimeStamp(gi.getShowAtTimeStamp());
		setLng(gi.getLng());
		setLat(gi.getLat());
		setPayload(gi.getPayload());
		setAutoId(gi.getAutoId());
		setTimeVisible(gi.isTimeVisible());
		setDependsOnVisible(gi.isDependsOnVisible());
	}
	
	public GeneralItem(String subclassPayload) {
		setType(this.getClass().getSimpleName());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getAutoId() {
		return autoId;
	}
	
	public void setAutoId(int autoId) {
		this.autoId = autoId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRadius() {
		if (radius == null ) return 0;
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Long getShowAtTimeStamp() {
		if (showAtTimeStamp == null) return 0l;
		return showAtTimeStamp;
	}

	public void setShowAtTimeStamp(Long showAtTimeStamp) {
		this.showAtTimeStamp = showAtTimeStamp;
	}

	public Double getLng() {
		if (lng == null) return 0d;
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		if (lat == null) return 0d;
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public JSONObject getSpecificPartAsJson() {
		return new JSONObject();
	}
	
	public String getSpecificPartAsJsonString() {
		return getSpecificPartAsJson().toString();
	}

	public GeoPoint getGeoPoint() {
		return new GeoPoint((int) (getLat() * 1E6), (int) (getLng() * 1E6));
	}
	
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

//	public void startActivity(Context ctx) {
//		Intent i = null;
////		if (getType().equals(MultipleChoiceTest.TYPE)) {
////			i = new Intent(new Intent(ctx, MultipleChoiceActivity.class));
////		}
////		if (getType().equals(AudioObject.class.getSimpleName())) {
////			i = new Intent(new Intent(ctx, AudioObjectActivity.class));
////		}
////		if (getType().equals(NarratorItem.class.getSimpleName())) {
////			i = new Intent(new Intent(ctx, NarratorItemActivity.class));
////		}
////		if (i != null) {
//			i = new Intent(new Intent(ctx, getCorrespondingActivity()));
//			i.putExtra(GeneralItem.ID, getId());
//			i.putExtra("generalItem", this);
//			ctx.startActivity(i);
////		}
//	}
//	
//	public Class getCorrespondingActivity() {
//		if (getType().equals(MultipleChoiceTest.TYPE)) {
//			 return MultipleChoiceActivity.class;
//		}
//		if (getType().equals(AudioObject.class.getSimpleName())) {
//			return  AudioObjectActivity.class;
//		}
//		if (getType().equals(VideoObject.class.getSimpleName())) {
//			return  VideoObjectActivity.class;
//		}
//		return  NarratorItemActivity.class;
//	}
	
	public long getRunId() {
		return runId;
	}
	public void setRunId(long runId) {
		this.runId = runId;
	}
	
	
	public boolean isDependsOnVisible() {
		return dependsOnVisible;
	}
	public void setDependsOnVisible(boolean dependsOnVisible) {
		this.dependsOnVisible = dependsOnVisible;
	}
	public boolean isTimeVisible() {
		return timeVisible;
	}
	public void setTimeVisible(boolean timeVisible) {
		this.timeVisible = timeVisible;
	}
//	public void onCreate(DBAdapter db) {
//		
//	}
	
	public int getIcon(){
		if (getType().equals("MultipleChoiceTest")) {
			return R.drawable.multiple_choice;
		} else 	if (getType().equals("OpenQuestion")) {
			return R.drawable.speechbubble_green;
		} else 	if (getType().equals("AudioObject")) {
			return R.drawable.audio_icon;
		}
		return 0;
	}
	
	public boolean isMessage() {
		return (getLat() == 0 && getLng() == 0);
	}
	
	public Dependency getDependency() {
		return new Dependency(getDependsOn());
		
	}

}
