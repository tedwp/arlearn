package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
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
		(new GeneralItemDependencyHandler()).addTaskToQueue(db.getContext());
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());

	}
	
	//TODO this method is double
	protected void generalItemToDb(DBAdapter db, GeneralItem item) {
		
		boolean newInsert = db.getGeneralItemAdapter().insert(item);
//		db.getGeneralItemVisibility().setVisibilityStatus(item.getId(), null, 0, GeneralItemVisibility.NOT_INITIALISED);
		db.getGeneralItemVisibility().resetAllRunsVisibility(item.getId());
	}
	
}
