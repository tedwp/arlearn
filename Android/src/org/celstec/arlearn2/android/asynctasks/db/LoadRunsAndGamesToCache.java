package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.db.DBAdapter;

import android.content.Context;
import android.os.Message;

public class LoadRunsAndGamesToCache  extends GenericTask implements  DatabaseTask {

	@Override
	public void execute(DBAdapter db) {
		db.getRunAdapter().queryAll();
		db.getGameAdapter().queryAll();
	}

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		
	}

}
