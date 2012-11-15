package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.game.Game;

import android.content.Context;

public class GameCache extends GenericCache {

	private static GameCache instance;

	private HashMap<Long, Game> gameMap = new HashMap<Long, Game>();

	private GameCache() {
	}

	public static GameCache getInstance() {
		if (instance == null) {
			instance = new GameCache();
		}
		return instance;
	}
	
	public Game getGame(long gameId) {
		if (gameMap == null) return null;
		return gameMap.get(gameId);
	}
	
	public void putGame(long gameId, Game game) {
		synchronized (gameMap) {
			gameMap.put(gameId, game);
		}
	}

//	public void reloadFromDb(final Context ctx) {
//		DBAdapter.getAdapter(ctx).getGameAdapter().queryAll(new GameResults() {
//			
//			@Override
//			public void onResults(Game[] games) {
//				for (Game g: games) {
//					gameMap.put(g.getGameId(), g);
//				}
//				ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
//			}
//		}) ;
//		
//	}

	public void putGame(Game g) {
		putGame(g.getGameId(), g);
		
	}
}
