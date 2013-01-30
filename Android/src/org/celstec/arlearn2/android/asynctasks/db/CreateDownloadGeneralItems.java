package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;

import android.content.Context;
import android.os.Message;

public class CreateDownloadGeneralItems extends GenericTask implements  DatabaseTask {

	public DownloadItem[] items;
	


	@Override
	public void execute(DBAdapter db) {
		for (DownloadItem item: items) {
			DownloadItem old = db.getMediaCacheGeneralItems().get(item.getItemId(), item.getLocalId());
			if (old == null) {
				db.getMediaCacheGeneralItems().create(item);
			} else if (!item.getRemoteUrl().equals(old.getRemoteUrl())) {
				db.getMediaCacheGeneralItems().update(item);
			}
		}
	}
	

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		runAfterTasks(ctx);
	}
}
