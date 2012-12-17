package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.activities.ListGamesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.client.GameClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class DeleteGameTask implements NetworkTask {

	private Context ctx;

	private Long gameId;
	
	public DeleteGameTask(Context ctx) {
		this.ctx = ctx;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();

		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.GAME_DELETE;
		m.sendToTarget();
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute() {
		try {
			Game g = GameClient.getGameClient().delete(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), getGameId());
			if (g.getError() == null) {
				GameCache.getInstance().deleteGame(gameId);
				ActivityUpdater.updateActivities(ctx, ListGamesActivity.class.getCanonicalName());
			}
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);		
		}
		
	}

}
