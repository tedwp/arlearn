package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;


import android.content.Context;
import android.os.Message;

public class InitDownloadStatusTask extends GenericTask implements DatabaseTask {
	
	private Long gameId;

	public InitDownloadStatusTask(Long gameId) {
		this.gameId = gameId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	@Override
	public void execute(DBAdapter db) {
		if (gameId != null ){
			for (DownloadItem di : db.getMediaCacheGeneralItems().getDownloadItemsForGame(getGameId())){
				MediaGeneralItemCache.getInstance(gameId).putReplicationstatus(di.getReplicated(), di);
			}
		}
//			for (Long id : db.getGeneralItemVisibility().getItemsNotYetInitialised(runId, gameId)) {
//				GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, id, getRunId(), 0, GeneralItemVisibility.NOT_INITIALISED);
//			}
		runAfterTasks(db.getContext());
	}

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();

	}

}
