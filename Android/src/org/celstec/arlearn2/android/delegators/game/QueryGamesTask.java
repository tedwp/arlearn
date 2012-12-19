package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;

import android.content.Context;
import android.os.Message;

public class QueryGamesTask implements DBAdapter.DatabaseTask {

	public void addTaskToQueue(Context ctx) {
		DatabaseHandler nwHandler = DBAdapter.getDatabaseThread(ctx);
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.sendToTarget();
	}

	@Override
	public void execute(DBAdapter db) {
		db.getGameAdapter().queryAll();
		ActivityUpdater.updateActivities(db.getContext(), ListExcursionsActivity.class.getCanonicalName());
	}

}
