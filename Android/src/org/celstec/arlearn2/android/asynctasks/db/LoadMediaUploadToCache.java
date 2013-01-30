package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;

import android.content.Context;
import android.os.Message;

public class LoadMediaUploadToCache  extends GenericTask implements  DatabaseTask {

	private long runId;
	
	public long getRunId() {
		return runId;
	}
	
	public LoadMediaUploadToCache(long runId) {
		this.runId = runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	@Override
	public void execute(DBAdapter db) {
		for (UploadItem ui: db.getMediaCacheUpload().allItemsForRun(runId)) {
			MediaUploadCache.getInstance(runId).put(ui);
		}
		
	}

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		
	}
}
