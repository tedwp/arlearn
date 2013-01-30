package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.client.ActionClient;
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
		final PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		try {
			if (runId == null) return;
			final ActionList al = ActionClient.getActionClient().getRunActions(pa.getFusionAuthToken(), runId);
			ActionsDelegator.getInstance().saveServerActionsToAndroidDb(ctx, al, pa.getUsername());			
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
		
	}
}
