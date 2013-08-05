package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.notification.RunModification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RunReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.RunModification";
	

	
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
		ActivityUpdater.updateActivities(context, ListRunsParticipateActivity.class.getCanonicalName());

	}
	

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
