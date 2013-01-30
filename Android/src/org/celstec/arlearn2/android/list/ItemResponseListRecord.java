package org.celstec.arlearn2.android.list;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ItemResponseListRecord extends GenericListRecord {

	private static SimpleDateFormat formatter = new SimpleDateFormat("d MMM - HH:mm:ss");

	public ItemResponseListRecord(Long runId, Response response) {
		setImageResourceId(R.drawable.cloud_up_48);
		Date d = new Date(response.getTimestamp());
		double percentage = 0;
		int dividePercentageBy = 0;
		try {
			JSONObject json = new JSONObject(response.getResponseValue());
			int statusAudio = 10;
			
			if (json.has("audioUrl")) {
				String audioUrl = json.getString("audioUrl");
				statusAudio = MediaUploadCache.getInstance(runId).getReplicationStatus(audioUrl);
				percentage += MediaUploadCache.getInstance(runId).getPercentageUploaded(audioUrl);
				dividePercentageBy++;
			}
			if (json.has("text")) {
				statusAudio =MediaCacheGeneralItems.REP_STATUS_DONE;
				percentage =1;
				dividePercentageBy++;
			}
			
			int statusImage = 10;
			if (json.has("imageUrl")) {
				String imageUrl = json.getString("imageUrl");
				statusImage = MediaUploadCache.getInstance(runId).getReplicationStatus(imageUrl);
				percentage += MediaUploadCache.getInstance(runId).getPercentageUploaded(imageUrl);
				dividePercentageBy++;
			}
			if (json.has("videoUrl")) {
				String videoUrl = json.getString("videoUrl");
				statusImage = MediaUploadCache.getInstance(runId).getReplicationStatus(videoUrl);
				percentage += MediaUploadCache.getInstance(runId).getPercentageUploaded(videoUrl);
				dividePercentageBy++;
			}
			
				percentage /= dividePercentageBy;
			

			if (statusImage != 10 || statusAudio != 10) {
				int status = Math.min(statusAudio, statusImage);
				switch (status) {
				case MediaCacheGeneralItems.REP_STATUS_TODO:
					setImageResourceId(R.drawable.cloud_up_48);
					break;
				case MediaCacheGeneralItems.REP_STATUS_SYNCING:
					setImageResourceId(R.drawable.cloud_sync_48);
					break;
				case MediaCacheGeneralItems.REP_STATUS_DONE:
					setImageResourceId(R.drawable.cloud_ok_48);
					break;

				default:
					break;
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		setMessageHeader(""+formatter.format(d));
		
		if (percentage == 1){
			setMessageDetail(null);
		} else{
			setMessageDetail(MessageFormat.format("{0,number,#.##%}", percentage));	
		}
	}
}
