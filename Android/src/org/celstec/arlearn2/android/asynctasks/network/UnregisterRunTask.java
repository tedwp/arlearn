package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.client.UserClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class UnregisterRunTask extends GenericTask implements NetworkTask {

	
	private long runId;
	private Context ctx;
	
	public UnregisterRunTask(long runId) {
		this.runId = runId;
	}
	
	@Override
	public void execute() {
		try {
			UserClient.getUserClient().deleteUser(PropertiesAdapter.getInstance(ctx).getAuthToken(), runId);
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}
		runAfterTasks(ctx);
	}
	
	@Override
	public void run(Context ctx) {
		this.ctx = ctx;
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.sendToTarget();
	}
}
