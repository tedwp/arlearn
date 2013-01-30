package org.celstec.arlearn2.android.asynctasks.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
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
import android.util.Log;

public class SynchronizeGeneralItemsTask implements NetworkTask {

	private Context ctx;
	private Long runId;
	
	private Long gameId;
	
	private static HashSet<Long> allIds = new HashSet<Long>();
	
	public SynchronizeGeneralItemsTask(Context ctx, Long runId) {
		this.ctx = ctx;
		this.runId = runId;
	}
	
	public SynchronizeGeneralItemsTask(Long gameId, Context ctx) {
		this(ctx, null);
		this.gameId = gameId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	private synchronized void addRunId(long runId) {
		allIds.add(this.runId);
	}
	
	@Override
	public void execute() {
		
		if (runId != null && runId > 0) {
			addRunId(runId);
			allIds.remove(runId);
			PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
			syncronizeGeneralItems(pa);
			syncronizeVisibleGeneralItems(pa);
			syncronizeDisappearedGeneralItems(pa);
		}
		if (gameId != null) {
			PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
			syncronizeGeneralItemsGame(pa);
		}
		(new GeneralItemDependencyHandler()).addTaskToQueue(ctx);
			
	}
	
	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (runId != null && !allIds.contains(runId)) {
			buildCache();
			addRunId(runId);
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GENERALITEMS;
			m.sendToTarget();
		}
		if (gameId != null) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GENERALITEMS;
			m.sendToTarget();
		}
	}

	
	private void syncronizeGeneralItems(PropertiesAdapter pa) {
		try {
			final GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItemsAll(pa.getFusionAuthToken(), runId);
			if (gl.getErrorCode() != null && gl.getErrorCode() == GeneralItemList.RUNNOTFOUND) {
//				notifyRunDeleted(ctx);
				RunDelegator.getInstance().synchronizeRunsWithServer(ctx);
			}
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {

				@Override
				public void execute(DBAdapter db) {
					Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
					Long gameId = null;
					while (it.hasNext()) {
						GeneralItem item = it.next();
						if (item != null) {						
							generalItemToDb(db, item);
							gameId = item.getGameId();
						} 
					}
					(new GeneralItemDependencyHandler()).addTaskToQueue(db.getContext());
				}
				
				protected void generalItemToDb(DBAdapter db, GeneralItem item) {
					boolean newInsert = db.getGeneralItemAdapter().insert(item);
					GeneralItemsDelegator.getInstance().createDownloadTasks(db.getContext(), item);
				}
			};
			m.sendToTarget();
			
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
		
	}
	
	private void syncronizeVisibleGeneralItems(PropertiesAdapter pa) {
		try {
			final GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItemsVisible(pa.getFusionAuthToken(), runId);
			final List<GeneralItem> newVisibleItems = new ArrayList<GeneralItem>();
			Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
			while (it.hasNext()) {
				GeneralItem item = it.next();
				if (!GeneralItemVisibilityCache.getInstance().isVisible(runId, item.getId())) {
					newVisibleItems.add(item);
				}
			}
			System.out.println("before loop"+newVisibleItems.size());
			if (!newVisibleItems.isEmpty()) {
				
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {

				@Override
				public void execute(DBAdapter db) {
					System.out.println("in execute"+newVisibleItems.size());
					System.out.println("in execute"+newVisibleItems);
					for (GeneralItem item : newVisibleItems) {
						System.out.println("item "+item);
						if (item.getVisibleAt() == null) {
							item.setVisibleAt(0l);
						}
						db.getGeneralItemVisibility().setVisibilityStatus(item.getId(), runId, item.getVisibleAt(), GeneralItemVisibility.VISIBLE);
					}
					ActivityUpdater.updateActivities(ctx, 
							ListMessagesActivity.class.getCanonicalName(), 
							MapViewActivity.class.getCanonicalName(), 
							ListMapItemsActivity.class.getCanonicalName(), 
							NarratorItemActivity.class.getCanonicalName());
					
				}
			};
			m.sendToTarget();
			}
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
	}
	
	private void syncronizeDisappearedGeneralItems(PropertiesAdapter pa) {
		try {
		final GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItemsDisappeared(pa.getFusionAuthToken(), runId);
		
		System.out.println(gl);
		final List<GeneralItem> newDissapearItems = new ArrayList<GeneralItem>();
		Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
		while (it.hasNext()) {
			GeneralItem item = it.next();
			long disAt = GeneralItemVisibilityCache.getInstance().disappearedAt(runId, item.getId());
			if (disAt == -1 || item.getDisappearAt() < disAt) {
				newDissapearItems.add(item);
			}
		}
		if (!newDissapearItems.isEmpty()) {
			
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {

				@Override
				public void execute(DBAdapter db) {
					for (GeneralItem item : newDissapearItems) {
						db.getGeneralItemVisibility().setVisibilityStatus(item.getId(), runId, item.getDisappearAt(), GeneralItemVisibility.NO_LONGER_VISIBLE);
					}
					ActivityUpdater.updateActivities(ctx, 
							ListMessagesActivity.class.getCanonicalName(), 
							MapViewActivity.class.getCanonicalName(), 
							ListMapItemsActivity.class.getCanonicalName(), 
							NarratorItemActivity.class.getCanonicalName());
					
				}
			};
			m.sendToTarget();
			}
		} catch (Exception e) {
			//TODO makee this exception go away
		}
	}

	
	private void syncronizeGeneralItemsGame(PropertiesAdapter pa) {
		try {
			final GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getGameGeneralItems(pa.getFusionAuthToken(), gameId);
			if (gl.getErrorCode() != null) {
				return;
			}
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {

				@Override
				public void execute(DBAdapter db) {
					Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
					while (it.hasNext()) {
						GeneralItem item = it.next();
						if (item != null) {						
							generalItemToDb(db, item);
						} 
					}
				}
				
				protected void generalItemToDb(DBAdapter db, GeneralItem item) {
					boolean newInsert = db.getGeneralItemAdapter().insert(item);
					GeneralItemsDelegator.getInstance().createDownloadTasks(db.getContext(), item);
				}
			};
			m.sendToTarget();
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
	}
	
//	private void notifyRunDeleted(Context context) {
//		RunModification rm = new RunModification();
//		rm.setModificationType(RunModification.DELETED);
//		rm.setRun(new Run());
//		rm.getRun().setRunId(runId);
//		Intent runIntent = new Intent();
//		runIntent.setAction(RunReceiver.action);
//		runIntent.putExtra("bean", rm);
//		context.sendBroadcast(runIntent);
//	}
	
	public void buildCache() {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj =  new DBAdapter.DatabaseTask () {

			@Override
			public void execute(DBAdapter db) {
				buildCache(db);
//				db.getMediaCacheGeneralItems().listItemsToCache();
			}
		};
		m.sendToTarget();
	}
	
	private void buildCache(DBAdapter db) {
		db.getMyActions().queryAll(db, runId);
		db.getGeneralItemAdapter().queryAll(db); //todo filter gameId
		db.getGeneralItemVisibility().queryAll(db, runId);
		//TODO uploadItems
//		db.getMediaCache().queryAll(db, runId);
		db.getMyResponses().queryAll(db, runId);
		ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName(),
				MapViewActivity.class.getCanonicalName(),
				ListMapItemsActivity.class.getCanonicalName(),
				NarratorItemActivity.class.getCanonicalName()
				);
		

	}

	
}
