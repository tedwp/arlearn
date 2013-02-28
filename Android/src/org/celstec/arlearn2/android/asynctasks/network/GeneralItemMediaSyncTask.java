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
package org.celstec.arlearn2.android.asynctasks.network;

import java.util.Set;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;

import android.content.Context;
import android.os.Message;

public class GeneralItemMediaSyncTask {//implements NetworkTask {
	
//	private Context ctx;
//	private Long gameId;
//
//	
//
//	public GeneralItemMediaSyncTask(Context ctx) {
//		this.ctx = ctx;
//	}
//
//	public GeneralItemMediaSyncTask(Context ctx, Long gameId) {
//		this(ctx);
//		this.gameId = gameId;
//	}
//	
//	public void addTaskToQueue(Context ctx) {
//		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
//		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_GI_MEDIA)) {
//			Message m = Message.obtain(nwHandler);
//			m.obj = this;
//			m.what = NetworkTaskHandler.SYNC_GI_MEDIA;
//			m.sendToTarget();
//		}
//	}
//	
//	@Override
//	public void execute() {
//		if (gameId != null) {
//			download(MediaGeneralItemCache.getInstance(gameId).getToDownloadItems());
//		}
//	}

//	private void download(DownloadItem itemIds[]) {
//		DownloadFileTask task = new DownloadFileTask();
//		task.ctx = ctx;
//		task.allDownloadItems = itemIds;
//		task.gameId = gameId;
//		
//		Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
//		m.obj = task;
//		m.sendToTarget();
//	}
	
	

}
