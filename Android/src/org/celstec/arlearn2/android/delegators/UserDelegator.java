package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.broadcast.task.SynchronizeUserTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.run.User;

import android.content.Context;
import android.os.Message;

public class UserDelegator {

	private static UserDelegator instance;

	private UserDelegator() {

	}

	public static UserDelegator getInstance() {
		if (instance == null) {
			instance = new UserDelegator();
		}
		return instance;
	}

	public void synchronizeUserWithServer(Context ctx, Long runId, String username) {
		(new SynchronizeUserTask(ctx, runId, username)).addTaskToQueue(ctx);
	}

	public void saveUser(Context ctx, final User u) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				db.getRunAdapter().updateUserRole(u);
			}
		};
		m.sendToTarget();
	}

}
