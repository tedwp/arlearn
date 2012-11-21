package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.beans.game.Game;

public class GameCache extends GenericCache {

	private static GameCache instance;

	private HashMap<Long, Game> gameMap = new HashMap<Long, Game>();

	private GameCache() {
	}

	public void empty() {
		gameMap = new HashMap<Long, Game>();
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

	public void putGame(Game g) {
		putGame(g.getGameId(), g);
		
	}
}
