package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class SynchronizeParticipatingGameTask implements NetworkTask {

	private Context ctx;
	private Long runId;

	public SynchronizeParticipatingGameTask(Context ctx) {
		this.ctx = ctx;
	}

	public SynchronizeParticipatingGameTask(Context ctx, Long runId) {
		this(ctx);
		this.runId = runId;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.SYNC_PARTICIPATING_GAME;
		m.sendToTarget();
	}
	
	@Override
	public void execute() {
		try {
			Run run = RunClient.getRunClient().getRun(runId, PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			if (run != null && run.getError() == null && run.getGame() != null) {
				GameDelegator.getInstance().saveServerGameToAndroidDb(ctx, run.getGame());
			}
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);

		}
		
	}

}
