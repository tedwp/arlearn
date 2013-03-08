package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;

import android.content.Context;
import android.os.Message;

public class LoadGeneralItemsFromDbTask extends GenericTask implements DatabaseTask {

	private Long gameId;
	private Long runId;

	
	public LoadGeneralItemsFromDbTask(Long gameId, Long runId) {
		super();
		this.gameId = gameId;
		this.runId = runId;
	}

	@Override
	public void execute(DBAdapter db) {
		if (gameId != null)
			GeneralItemsCache.getInstance().put(db.getGeneralItemAdapter().queryByGameId(gameId));
		if (runId != null) {
			db.getGeneralItemVisibility().query(runId, org.celstec.arlearn2.android.db.GeneralItemVisibility.VISIBLE);
			db.getGeneralItemVisibility().query(runId, org.celstec.arlearn2.android.db.GeneralItemVisibility.NO_LONGER_VISIBLE);
		}
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());
		runAfterTasks(db.getContext());

	}

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();

	}
}
