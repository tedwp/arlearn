package org.celstec.arlearn2.cache;

import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.ProgressDefinition;

import com.google.appengine.api.utils.SystemProperty;

public class ProgressDefinitionCache extends GameCache{

	private static ProgressDefinitionCache instance;

	private static final Logger logger = Logger.getLogger(ProgressDefinitionCache.class.getName());
	private static String PROGRESSDEFINITION_PREFIX = SystemProperty.applicationVersion.get()+"ProgressDefinition";

	private ProgressDefinitionCache() {
	}

	public static ProgressDefinitionCache getInstance() {
		if (instance == null)
			instance = new ProgressDefinitionCache();
		return instance;

	}
	
	public List<ProgressDefinition> getProgressDefinitionList(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(PROGRESSDEFINITION_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (List<ProgressDefinition>) getCache().get(cacheKey);
	}
	
	public void putProgressDefinitionList(List<ProgressDefinition> pdList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(PROGRESSDEFINITION_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(cachekey, pdList);
	}
	
	public void removeProgressDefinitonList(Long gameId) {
		removeKeysForGame(gameId);
	}


	@Override
	protected String getType() {
		return "ProgressDefinitionCache";
	}
}
