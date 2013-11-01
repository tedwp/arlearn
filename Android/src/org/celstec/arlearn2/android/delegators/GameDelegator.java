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
package org.celstec.arlearn2.android.delegators;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeMyGamesTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeParticipateGamesTask;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.delegators.game.CreateGameTask;
import org.celstec.arlearn2.android.delegators.game.DeleteGameTask;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.beans.game.GamesList;

import android.content.Context;
import android.os.Message;
import android.util.Log;

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
	
	public void synchronizeParticipateGamesWithServer(Context ctx) {
		(new SynchronizeParticipateGamesTask(ctx)).run(ctx);
	}

	public void synchronizeMyGamesWithServer(Context ctx) {
		(new SynchronizeMyGamesTask(ctx)).run(ctx);
	}
	
	public void saveServerGamesToAndroidDb(final Context ctx, final GamesList gl, final GameAccessList gal) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				
				Iterator<Game> it = gl.getGames().iterator();
				boolean updateOccured = false;
				while (it.hasNext()) {
					Game game = it.next();
					Log.i("GAMEPROBLEM", "iterating over game "+game);
					if (game.getError() == null) {
						GameCache.getInstance().putGame(game);
						updateOccured =  db.getGameAdapter().insertGame(game)||updateOccured;
					} 
				}
				
				Iterator<GameAccess> itAccess = gal.getGameAccess().iterator();
				while (itAccess.hasNext()) {
					GameAccess gameAccess = itAccess.next();
					Log.i("GAMEPROBLEM", "iterating over game access "+gameAccess);
					if (gameAccess.getError() == null) {
						GameCache.getInstance().putGameAccess(gameAccess);
					} 
				}				
				
				if (updateOccured) {
					ActivityUpdater.updateActivities(ctx, ListRunsParticipateActivity.class.getCanonicalName());
				}

			}
		};
		m.sendToTarget();
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
					Log.i("GAMEPROBLEM", "iterating over game "+game);
					if (game.getError() == null) {
						GameCache.getInstance().putGame(game);
						updateOccured =  db.getGameAdapter().insertGame(game)||updateOccured;
					} 
				}

				if (updateOccured) {
					ActivityUpdater.updateActivities(ctx, ListRunsParticipateActivity.class.getCanonicalName());
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
				ActivityUpdater.updateActivities(ctx, ListRunsParticipateActivity.class.getCanonicalName());
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
				ActivityUpdater.updateActivities(ctx, ListRunsParticipateActivity.class.getCanonicalName());
			}
		};
		m.sendToTarget();
	}

	public void deleteGame(Context ctx, long gameId) {
		DeleteGameTask dgTask = new DeleteGameTask(ctx);
		dgTask.setGameId(gameId);
		dgTask.addTaskToQueue(ctx);
	}

	public int getAmountOfUncachedItems(long gameId) {
		return MediaGeneralItemCache.getInstance(gameId).getAmountOfItemsToDownload();
	}

	public void createGame(Context ctx, String gameTitle, String gameAuthor, boolean withMap) {
		Game newGame = new Game();
		newGame.setCreator(gameAuthor);
		newGame.setTitle(gameTitle);
		
		Config c = new Config();
		c.setMapAvailable(withMap);
		newGame.setConfig(c);
		
		createGame(ctx, newGame);
	}
	
	public void createGame(Context ctx, Game game) {
		CreateGameTask cgTask = new CreateGameTask(ctx, game);
		cgTask.addTaskToQueue(ctx);
	}
}
