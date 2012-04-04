package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.ScoreDefinition;

import net.sf.jsr107cache.Cache;

public class ScoreDefinitionCache {

	private static ScoreDefinitionCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ScoreDefinitionCache.class
			.getName());

	private ScoreDefinitionCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ScoreDefinitionCache getInstance() {
		if (instance == null)
			instance = new ScoreDefinitionCache();
		return instance;
	}
	
	private static String SCOREDEFINITION_PREFIX = "ScoreDefinition";

	public ScoreDefinition getScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		return (ScoreDefinition) cache.get(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}

	public void putScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType, ScoreDefinition sd) {
		cache.put(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType), sd);
	}
	
	public void removeScoreDefinition(Long gameId, String action, String generalItemId, String generalItemType) {
		cache.remove(FusionCache.getCacheKey(SCOREDEFINITION_PREFIX, "" + gameId, action, generalItemId, generalItemType));
	}
	
	public int getScoreDefinitionTableIdForGame(String authToken, Long gameId) {
		Integer returnInt = (Integer) cache.get(SCOREDEFINITION_PREFIX+authToken+gameId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putScoreDefinitionTableIdForGame(String authToken, Long gameId, Integer tableId) {
		cache.put(SCOREDEFINITION_PREFIX+authToken+gameId, tableId);
	}
	
	
	public ScoreDefinition getScoreDefinition(Long gameId, String actionId) {
		return (ScoreDefinition) cache.get(SCOREDEFINITION_PREFIX+gameId+actionId);
	}
	
	public void putScoreDefinition(Long gameId, String actionId, ScoreDefinition sd) {
		cache.put(SCOREDEFINITION_PREFIX+gameId+actionId, sd);
	}
	


}
