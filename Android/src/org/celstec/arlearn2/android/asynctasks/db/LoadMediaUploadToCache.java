/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;

import android.content.Context;
import android.os.Message;

public class LoadMediaUploadToCache extends GenericTask implements DatabaseTask {

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
		for (UploadItem ui : db.getMediaCacheUpload().allItemsForRun(runId)) {
			MediaUploadCache.getInstance(runId).put(ui);
		}

	}

	@Override
	public void run(Context ctx) {
		if (NetworkSwitcher.isOnline(ctx)) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = this;
			m.sendToTarget();
		}
	}
}
