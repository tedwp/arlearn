package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.client.ActionClient;

import android.content.Context;
import android.os.Message;

public class PublishActionTask implements NetworkTask {

	private Action action;
	private Context ctx;

	public PublishActionTask(Context ctx, Action action) {
		this.action = action;
		this.ctx = ctx;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.PUBLISH_ACTION;
		m.sendToTarget();
	}

	@Override
	public void execute() {
		ActionClient ac = ActionClient.getActionClient();
		Action result = ac.publishAction(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), action);
		result.setTime(action.getTime());
		if (result.getError() == null) {
			ActionsDelegator.getInstance().confirmReplicated(ctx, result);
//			DBAdapter.getAdapter(ctx).getMyActions().confirmReplicated(result);
		} else {
			if (result.getError() != null && "User not found".equals(result.getError()))
				ActionsDelegator.getInstance().confirmReplicated(ctx, result);
//				DBAdapter.getAdapter(ctx).getMyActions().confirmReplicated(result);
			// this is not elegant... but its mean that the user was deleted, so don't try to sync in future
		}

	}
}
