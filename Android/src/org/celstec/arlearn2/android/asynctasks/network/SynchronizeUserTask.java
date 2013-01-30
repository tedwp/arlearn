package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.delegators.UserDelegator;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.UserClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeUserTask implements NetworkTask {

	public static long lastSyncWithCloud = 0 ;
	public static final long timeBetweenTwoCloudSyncs = 300000;

	public Context ctx;
	public Long runId;
	public String username;

	public SynchronizeUserTask(Context ctx, Long runId, String username) {
		this.ctx = ctx;
		this.runId = runId;
		this.username = username;
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
			PropertiesAdapter pa  = PropertiesAdapter.getInstance(ctx);
			User u = UserClient.getUserClient().getUser(pa.getFusionAuthToken(), runId, username);
			UserDelegator.getInstance().saveUser(ctx, u);
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}

		}
	}
}
