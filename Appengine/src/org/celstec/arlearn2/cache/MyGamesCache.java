package org.celstec.arlearn2.cache;

import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ProgressDefinition;

import com.google.appengine.api.utils.SystemProperty;


public class MyGamesCache extends GenericCache{
	private static MyGamesCache instance;

	private static final Logger logger = Logger.getLogger(MyGamesCache.class.getName());
	private static String MYGAMES_PREFIX = SystemProperty.applicationVersion.get()+"MyGamesCache";

	private MyGamesCache() {
	}

	public static MyGamesCache getInstance() {
		if (instance == null)
			instance = new MyGamesCache();
		return instance;

	}
	
	public List<Game> getGameList(Long gameId, Object... args) {
		return (List<Game>) getCache().get(generateCacheKey(MYGAMES_PREFIX, gameId, args));
	}

	public void putGameList(List<Game> gameList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(MYGAMES_PREFIX, gameId, args); 
		getCache().put(cachekey, gameList);
	}
	
	public void removeGameList(Long gameId, Object... args) {
		String cachekey = generateCacheKey(MYGAMES_PREFIX, gameId, args); 
		getCache().remove(cachekey);
	}
	
}
