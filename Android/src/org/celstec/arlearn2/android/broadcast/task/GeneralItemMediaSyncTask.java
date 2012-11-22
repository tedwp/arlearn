package org.celstec.arlearn2.android.broadcast.task;

import java.util.Set;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.MediaCache;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.content.Context;
import android.os.Message;

public class GeneralItemMediaSyncTask implements NetworkTask {
	
	private Context ctx;
	private Long gameId;

	

	public GeneralItemMediaSyncTask(Context ctx) {
		this.ctx = ctx;
	}

	public GeneralItemMediaSyncTask(Context ctx, Long gameId) {
		this(ctx);
		this.gameId = gameId;
	}
	
	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_GI_MEDIA)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GI_MEDIA;
			m.sendToTarget();
		}
	}
	
	@Override
	public void execute() {
		if (gameId != null) {
			Set<Long> itemIds = MediaCache.getInstance().getToDownloadItems(gameId);
			if (itemIds != null && !itemIds.isEmpty()) {
				download(itemIds.toArray(new Long[]{}));
				
			}
		}
	}

	private void download(Long itemIds[]) {
		DownloadFileTask task = new DownloadFileTask();
		task.ctx = ctx;
		task.allIds = itemIds;
		task.gameId = gameId;
		
		Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
		m.obj = task;
		m.sendToTarget();
	}
	
	

}
