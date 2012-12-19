package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeRunsTask;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class RunReceiver extends GenericReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.RunModification";
	
//	public static long lastSyncWithCloud = 0 ;
//	public static final long timeBetweenTwoCloudSyncs = 300000;

	// private Semaphore semaphore;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			RunModification bean = (RunModification) extras.getSerializable("bean");
			if (bean != null) {
				databaseOperations(context, bean);
			}
		} else {
//			(new SynchronizeRunsTask(context)).addTaskToQueue(context);
//			buildCache(context);
		}
		ActivityUpdater.updateActivities(context, ListExcursionsActivity.class.getCanonicalName());

	}
	
//	private void buildCache(final Context ctx) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
//		m.obj = new DBAdapter.DatabaseTask() {
//
//			@Override
//			public void execute(DBAdapter db) {
//				
//				db.getGameAdapter().queryAll();
//				
//			}
//		};
//		m.sendToTarget();
//
//	}

	private void databaseOperations(Context ctx, RunModification rm) {
		boolean updateGeneralItems = false;
		switch (rm.getModificationType()) {
		case RunModification.CREATED:
			RunDelegator.getInstance().saveServerRunToAndroidDb(ctx, rm.getRun());
//			DBAdapter.getAdapter(ctx).getRunAdapter().insert(rm.getRun());
			updateGeneralItems = true;
			Intent gimIntent = new Intent();
			gimIntent.putExtra("runId", rm.getRun().getRunId()); // TODO
																	// probably
																	// update
																	// via
																	// gameId
			gimIntent.setAction(GeneralItemReceiver.action);
			ctx.sendBroadcast(gimIntent);
			break;
		case RunModification.DELETED:
			DBAdapter.getAdapter(ctx).getRunAdapter().delete(rm.getRun().getRunId());
			break;
		case RunModification.ALTERED: //TODO do we really want to delete all Run data?
			DBAdapter.getAdapter(ctx).getRunAdapter().delete(rm.getRun().getRunId());
	
			RunDelegator.getInstance().saveServerRunToAndroidDb(ctx, rm.getRun());
//			DBAdapter.getAdapter(ctx).getRunAdapter().insert(rm.getRun());

			updateGeneralItems = true;
			break;
		default:
			break;
		}
		if (updateGeneralItems) {
			Intent gimIntent = new Intent();
			gimIntent.setAction(GeneralItemReceiver.action);
			ctx.sendBroadcast(gimIntent);
		}
	}
	
}
