package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;

import android.content.Context;
import android.os.Message;

public class CleanUpFilesThatAreNotInDatabase extends GenericTask implements  DatabaseTask {

	@Override
	public void execute(DBAdapter db) {
		UploadItem[] items = db.getMediaCacheUpload().allItems();
		for (UploadItem item : items) {
			System.out.println("item "+item.getUri().getPath());		
			//TODO
		}
	}
	

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		
	}

}
