package org.celstec.arlearn2.android.broadcast.task;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.broadcast.RunReceiver;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.MyActions.PublishAction;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

public class SynchronizeGeneralItemsTask implements NetworkTask {

	public Context ctx;
	public Long runId;

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	@Override
	public void execute() {
		if (runId == null) setRunId();
		if (runId != null && runId > 0) {
			buildCache();
			syncronizeGeneralItems();
		}
			
	}


	private void setRunId() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		runId = pa.getCurrentRunId();
		syncronizeGeneralItems();
	}
	
	private void syncronizeGeneralItems() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		try {
			if (runId == null) return;
			final GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItems(pa.getFusionAuthToken(), runId);

			if (gl.getErrorCode() != null && gl.getErrorCode() == GeneralItemList.RUNNOTFOUND) {
				notifyRunDeleted(ctx);
			}

			
			
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {

				@Override
				public void execute(DBAdapter db) {
					Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
					while (it.hasNext()) {
						GeneralItem item = it.next();
						if (item != null) {
							
							item.setRunId(runId);
							generalItemToDb(db, item);
						} else {
							System.out.println("break");
						}
						
					}
					(new GeneralItemDependencyHandler(ctx)).checkDependencies(db);
					ActivityUpdater.updateActivities(ctx, 
							ListMessagesActivity.class.getCanonicalName(), 
							MapViewActivity.class.getCanonicalName(), 
							ListMapItemsActivity.class.getCanonicalName(), 
							NarratorItemActivity.class.getCanonicalName());
					
				}
				
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
			};
			m.sendToTarget();
			
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
		
	}
	
	private void notifyRunDeleted(Context context) {
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED);
		rm.setRun(new Run());
		rm.getRun().setRunId(runId);
		Intent runIntent = new Intent();
		runIntent.setAction(RunReceiver.action);
		runIntent.putExtra("bean", rm);
		context.sendBroadcast(runIntent);
	}
	
	private void buildCache() {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj =  new DBAdapter.DatabaseTask () {

			@Override
			public void execute(DBAdapter db) {
				buildCache(db);
			}
		};
		m.sendToTarget();
	}
	
	private void buildCache(DBAdapter db) {
 
		db.getMyActions().queryAll(db, runId);
		db.getGeneralItemAdapter().queryAll(db); //todo filter gameId
		db.getGeneralItemVisibility().queryAll(db, runId);
		db.getMediaCache().queryAll(db, runId);
		db.getMyResponses().queryAll(db, runId);
		
		ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName(),
				MapViewActivity.class.getCanonicalName(),
				ListMapItemsActivity.class.getCanonicalName(),
				NarratorItemActivity.class.getCanonicalName()
				);
		

	}

	
}
