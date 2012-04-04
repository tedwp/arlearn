package org.celstec.arlearn2.cache;

import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.util.FusionCache;

public class ScoreDefinitionCache extends GameCache{

	private static ScoreDefinitionCache instance;

	private static final Logger logger = Logger.getLogger(UsersCache.class.getName());
	private static String SCOREDEFINITION_PREFIX = "ScoreDefinition";

	private ScoreDefinitionCache() {
	}

	public static ScoreDefinitionCache getInstance() {
		if (instance == null)
			instance = new ScoreDefinitionCache();
		return instance;

	}
	
	public List<ScoreDefinition> getScoreDefinitionList(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (List<ScoreDefinition>) getCache().get(generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args));
	}
	
	public void putScoreDefinitionList(List<ScoreDefinition> scList, Long gameId, Object... args) {
		String cachekey = FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(SCOREDEFINITION_PREFIX, gameId, args), scList);
	}
	
	public void removeScoreDefinitonList(Long gameId) {
		removeKeysForGame(gameId);
	}


	@Override
	protected String getType() {
		return "ScoreDefinitionCache";
	}
}
