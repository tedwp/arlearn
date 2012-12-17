package org.celstec.arlearn2.android.delegators;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListGamesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeGamesTask;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeUserTask;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.delegators.game.CreateGameTask;
import org.celstec.arlearn2.android.delegators.game.DeleteGameTask;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.run.RunList;

import android.content.Context;
import android.os.Message;

public class GameDelegator {
	
	private static GameDelegator instance;

	private GameDelegator() {

	}

	public static GameDelegator getInstance() {
		if (instance == null) {
			instance = new GameDelegator();
		}
		return instance;
	}
	
	public void synchronizeGameWithServer(Context ctx, Long gameId) {
		(new SynchronizeGamesTask(ctx, gameId)).addTaskToQueue(ctx);
	}
	
	public void fetchMyGamesFromServer(Context ctx) {
		(new SynchronizeGamesTask(ctx)).addTaskToQueue(ctx);
	}
	
	public void saveServerGamesToAndroidDb(final Context ctx, final GamesList gl) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				Iterator<Game> it = gl.getGames().iterator();
				boolean updateOccured = false;
				while (it.hasNext()) {
					Game game = it.next();
					GameCache.getInstance().putGame(game);
					updateOccured = updateOccured || db.getGameAdapter().insertGame(game);
				}
				if (updateOccured) {
					ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName(), ListGamesActivity.class.getCanonicalName());
				}

			}
		};
		m.sendToTarget();
	}
	
	public void saveServerGameToAndroidDb(final Context ctx, final Game game) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				db.getMediaCacheGeneralItems().listItemsToCache();
				GameCache.getInstance().putGame(game);
				db.getGameAdapter().insertGame(game);
				ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
			}
		};
		m.sendToTarget();
	}
	
	public void createGame(Context ctx, String gameTitle, String gameAuthor, boolean withMap) {
		CreateGameTask cgTask = new CreateGameTask(ctx);
		cgTask.setAuthor(gameAuthor);
		cgTask.setGameTitle(gameTitle);
		cgTask.setWithMap(withMap);
		cgTask.addTaskToQueue(ctx);
	}

	public void deleteGame(Context ctx, long gameId) {
		DeleteGameTask dgTask = new DeleteGameTask(ctx);
		dgTask.setGameId(gameId);
		dgTask.addTaskToQueue(ctx);
		
	}
}
