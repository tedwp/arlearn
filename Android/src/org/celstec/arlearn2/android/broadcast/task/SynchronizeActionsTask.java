package org.celstec.arlearn2.android.broadcast.task;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.client.ActionClient;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeActionsTask implements NetworkTask {
	
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
//			buildCache();
			syncronizeActions();
		}
			
	}
	
	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_ACTIONS)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_ACTIONS;
			m.sendToTarget();
		}
	}

	private void setRunId() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		runId = pa.getCurrentRunId();
		syncronizeActions();
	}

	private void syncronizeActions() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		try {
			if (runId == null) return;
			final ActionList al = ActionClient.getActionClient().getRunActions(pa.getFusionAuthToken(), runId);
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj =  new DBAdapter.DatabaseTask () {
				@Override
				public void execute(DBAdapter db) {
					Iterator<Action> it = al.getActions().iterator();
					while (it.hasNext()) {
						Action a = it.next();
						if (a != null) {
							db.getMyActions().insert(a, true);
						} else {
							System.out.println("break");
						}		
					}
					(new GeneralItemDependencyHandler()).addTaskToQueue(db.getContext());

//					(new GeneralItemDependencyHandler(ctx)).checkDependencies(db);
//					ActivityUpdater.updateActivities(ctx, 
//							ListMessagesActivity.class.getCanonicalName(), 
//							MapViewActivity.class.getCanonicalName(), 
//							ListMapItemsActivity.class.getCanonicalName());
					
				}
			};
			m.sendToTarget();
			
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
		
	}
}
