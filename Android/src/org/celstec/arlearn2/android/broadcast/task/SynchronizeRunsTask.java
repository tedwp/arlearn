package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeRunsTask implements NetworkTask {

	public static long lastSyncWithCloud = 0 ;
	public static final long timeBetweenTwoCloudSyncs = 300000;

	public Context ctx;

	public SynchronizeRunsTask(Context ctx) {
		this.ctx = ctx;
	}
	
	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_RUNS)) {
			if (lastSyncWithCloud == 0 || (lastSyncWithCloud + timeBetweenTwoCloudSyncs) < System.currentTimeMillis()) {

			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_RUNS;
			m.sendToTarget();
			}
		}
	}
	
	public static void resetCloudSyncTime() {
		lastSyncWithCloud = 0;
	}
	
	@Override
	public void execute() {
		try {
			final RunList rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = new DBAdapter.DatabaseTask() {

				@Override
				public void execute(DBAdapter db) {
					if (rl.getError() == null) {
						db.getRunAdapter().insert(rl.getRuns());
					}

				}
			};
			m.sendToTarget();

		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}

		}
	}

}
