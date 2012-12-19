package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class SynchronizeGamesTask implements NetworkTask {

	private Context ctx;
	private Long gameId;

	public SynchronizeGamesTask(Context ctx) {
		this.ctx = ctx;
	}

	public SynchronizeGamesTask(Context ctx, Long gameId) {
		this(ctx);
		this.gameId = gameId;
	}

	@Override
	public void execute() {
		if (gameId == null) {
			synchronizeGames();
		} else {
			synchronizeGame();
		}
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_GAMES)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GAMES;
			m.sendToTarget();
		}
	}

	private void synchronizeGames() {
		try {
			GamesList gl = GameClient.getGameClient().getGames(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			if (gl.getError() == null) {
				GameDelegator.getInstance().saveServerGamesToAndroidDb(ctx, gl);
			}
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);

		}
	}

	private void synchronizeGame() {
		try {
			Game game = GameClient.getGameClient().getGame(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), gameId);
			if (game.getError() == null) {
				GameDelegator.getInstance().saveServerGameToAndroidDb(ctx, game);

//				gameToDb(game);
			}
			(new SynchronizeGeneralItemsTask(game.getGameId(), ctx)).addTaskToQueue(ctx);
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);

		}
	}

//	private void gamesToDb(final List<Game> games) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
//		m.obj = new DBAdapter.DatabaseTask() {
//
//			@Override
//			public void execute(DBAdapter db) {
//				Iterator<Game> it = games.iterator();
//				boolean updateOccured = false;
//				while (it.hasNext()) {
//					Game game = it.next();
//					updateOccured = updateOccured || db.getGameAdapter().insertGame(game);
//				}
//				if (updateOccured) {
//					ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
//				}
//			}
//
//		};
//		m.sendToTarget();
//	}

//	private void gameToDb(final Game game) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
//		m.obj = new DBAdapter.DatabaseTask() {
//
//			@Override
//			public void execute(DBAdapter db) {
//				db.getMediaCacheGeneralItems().listItemsToCache();
//				db.getGameAdapter().insertGame(game);
//				ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
//
//			}
//
//		};
//		m.sendToTarget();
//	}
}
