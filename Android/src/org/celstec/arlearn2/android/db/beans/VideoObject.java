package org.celstec.arlearn2.android.db.beans;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VideoObject extends NarratorItem{

	private String videoFeed;
	
	public VideoObject() {
		super();
	}

	public VideoObject(GeneralItem gi) {
		super(gi);
		if (gi.getPayload() != null) {
			try {
				JSONObject object = new JSONObject(gi.getPayload());
				if (object != null) {
					if (object.has("videoFeed")) {
						setVideoFeed(object.getString("videoFeed"));
					}
				}
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}
	
	public String getVideoFeed() {
		return videoFeed;
	}

	public void setVideoFeed(String videoFeed) {
		this.videoFeed = videoFeed;
	}

	public JSONObject getSpecificPartAsJson() {
		JSONObject json = super.getSpecificPartAsJson();
		try {
			if (getVideoFeed() != null)json.put("videoFeed", getVideoFeed());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

//	@Override
//	public void onCreate(DBAdapter db) {
//		((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(getId(), getVideoFeed(), getRunId());
//		MediaCacheSyncroniser.getInstance().resetDelay();
//	}
	
	
}
