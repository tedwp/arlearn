package org.celstec.arlearn2.util;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import net.sf.jsr107cache.Cache;

public class GamesCache {

	private static GamesCache instance;
	private Cache cache;

	private GamesCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static GamesCache getInstance() {
		if (instance == null)
			instance = new GamesCache();
		return instance;
	}
	private static String GAMES_PREFIX = "Games";

//	public void removeGames(String authToken) {
//		cache.remove(GAMES_PREFIX+authToken);
//	}
//	
//	public GamesList getGames(String authToken) {
//		return (GamesList) cache.get(GAMES_PREFIX+authToken);
//	}
//	
//	public void putGames(String authToken, GamesList gl) {
//		cache.put(GAMES_PREFIX+authToken, gl);
//	}
//	
//	public Game getGame(String authToken, long gameId) {
//		return (Game) cache.get(GAMES_PREFIX+gameId+authToken);
//	}
//	
//	public void putGame(String authToken, long gameId, Game g) {
//		cache.put(GAMES_PREFIX+gameId+authToken, g);
//	}
}
