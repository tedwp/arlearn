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

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeGamesTask;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;

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
		(new SynchronizeGamesTask(ctx)).run(ctx);
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

//	public void downloadGameContent(final Context ctx, Long gameId) {
//		if (gameId != null) (new SynchronizeGeneralItemsTaskOld(gameId, ctx)).addTaskToQueue(ctx);
//	}
	
	public int getAmountOfUncachedItems(long gameId) {
		return MediaGeneralItemCache.getInstance(gameId).getAmountOfItemsToDownload();
	}

}
