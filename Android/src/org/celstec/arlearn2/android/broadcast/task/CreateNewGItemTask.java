package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.client.GeneralItemClient;

public class CreateNewGItemTask implements  DBAdapter.DatabaseTask {

	private String authToken;
	private Long runId;
	private Long generalItemId;
	
	public String getAuthToken() {
		return authToken;
	}



	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}



	public Long getRunId() {
		return runId;
	}



	public void setRunId(Long runId) {
		this.runId = runId;
	}



	public Long getGeneralItemId() {
		return generalItemId;
	}



	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}



	@Override
	public void execute(DBAdapter db) {
		GeneralItem item = GeneralItemClient.getGeneralItemClient().getRunGeneralItem(authToken, runId, generalItemId);
		generalItemToDb(db, item);
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());

	}
	
	//TODO this method is double
	protected void generalItemToDb(DBAdapter db, GeneralItem item) {
		boolean newInsert = db.getGeneralItemAdapter().insert(item);
		if (newInsert) {
			if (item instanceof AudioObject) {
				AudioObject aItem = (AudioObject) item;
				((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(aItem.getId(), aItem.getAudioFeed(), aItem.getRunId());
				MediaCacheSyncroniser.getInstance().resetDelay();
			}
			if (item instanceof VideoObject) {
				VideoObject vItem = (VideoObject) item;
				((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(vItem.getId(), vItem.getVideoFeed(), vItem.getRunId());
				MediaCacheSyncroniser.getInstance().resetDelay();
			}
		}
	}
	
}
