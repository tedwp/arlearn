package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeRunsTask implements NetworkTask {

//	public static long lastSyncWithCloud = 0;
//	public static final long timeBetweenTwoCloudSyncs = 300000;

	public Context ctx;

	public SynchronizeRunsTask(Context ctx) {
		this.ctx = ctx;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_RUNS)) {
//			if (lastSyncWithCloud == 0 || (lastSyncWithCloud + timeBetweenTwoCloudSyncs) < System.currentTimeMillis()) {
				Message m = Message.obtain(nwHandler);
				m.obj = this;
				m.what = NetworkTaskHandler.SYNC_RUNS;
				m.sendToTarget();
//			}
		}
	}
	

	@Override
	public void execute() {
		try {
			RunList rl = null;
			Long lastDate= PropertiesAdapter.getInstance(ctx).getRunLastSynchronizationDate()-120000;
			if (lastDate <= 0) {
				rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken()); 
			} else {
				rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), lastDate); 
			}
			 PropertiesAdapter.getInstance(ctx).setRunLastSynchronizationDate(rl.getServerTime());
			RunDelegator.getInstance().saveServerRunsToAndroidDb(ctx, rl);
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}

		}
	}

}
