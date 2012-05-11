package org.celstec.arlearn2.android.db.beans;


import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AudioObject extends NarratorItem{

	private String audioFeed;
	

	public AudioObject() {
		super();
	}

	public AudioObject(GeneralItem gi) {
		super(gi);
		if (gi.getPayload() != null) {
			try {
				JSONObject object = new JSONObject(gi.getPayload());
				if (object != null) {
					if (object.has("audioFeed")) {
						setAudioFeed(object.getString("audioFeed"));
					}
				}
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}

	public String getAudioFeed() {
		return audioFeed;
	}

	public void setAudioFeed(String audioFeed) {
		this.audioFeed = audioFeed;
	}
	
	public JSONObject getSpecificPartAsJson() {
		JSONObject json = super.getSpecificPartAsJson();
		try {
			if (getAudioFeed() != null)json.put("audioFeed", getAudioFeed());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

//	@Override
//	public void onCreate(DBAdapter db) {
//		((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(getId(), getAudioFeed(), getRunId());
//		MediaCacheSyncroniser.getInstance().resetDelay();
//	}
	
	
}
