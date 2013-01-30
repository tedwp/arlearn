package org.celstec.arlearn2.android.delegators;

import java.text.MessageFormat;
import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeGamesTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeUserTask;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.MediaCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
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
	
	public Game getGame(Long gameId) {
		return GameCache.getInstance().getGame(gameId);
	}
	
	public void synchronizeGamesWithServer(Context ctx) {
		(new SynchronizeGamesTask(ctx)).addTaskToQueue(ctx);
	}
	
	@Deprecated //don't do this one by one
	public void synchronizeGameWithServer(Context ctx, Long gameId) {
//		(new SynchronizeGamesTask(ctx, gameId)).addTaskToQueue(ctx);
	}
	
	public void saveServerGamesToAndroidDb(final Context ctx, final GamesList gl) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				System.out.println("about to update "+gl.getGames().size()+ " games");
				Iterator<Game> it = gl.getGames().iterator();
				boolean updateOccured = false;
				while (it.hasNext()) {
					Game game = it.next();
					GameCache.getInstance().putGame(game);
					updateOccured =  db.getGameAdapter().insertGame(game)||updateOccured;
				}
				if (updateOccured) {
					ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
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
				db.getMediaCacheGeneralItems().addToCache(game.getGameId());
				GameCache.getInstance().putGame(game);
				db.getGameAdapter().insertGame(game);
				ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
			}
		};
		m.sendToTarget();
	}
	
	public void loadGameToCache(final Context ctx, final Long gameId) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				db.getMediaCacheGeneralItems().addToCache(gameId);
				ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
			}
		};
		m.sendToTarget();
	}

	public void downloadGameContent(final Context ctx, Long gameId) {
		if (gameId != null) (new SynchronizeGeneralItemsTask(gameId, ctx)).addTaskToQueue(ctx);
	}
	
	public int getAmountOfUncachedItems(long gameId) {
		return MediaGeneralItemCache.getInstance(gameId).getAmountOfItemsToDownload();
	}

}
