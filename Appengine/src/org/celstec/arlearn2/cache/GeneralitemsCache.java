package org.celstec.arlearn2.cache;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.delegators.ActionRelevancyPredictor;

import com.google.appengine.api.utils.SystemProperty;

public class GeneralitemsCache extends GameCache {
	private static GeneralitemsCache instance;

	private static String GENERALITEM_PREFIX = SystemProperty.applicationVersion.get()+"GeneralItemDefinition";
	private static String ACTION_RELEVANCY_PREFIX = SystemProperty.applicationVersion.get()+"ActionRelevancy";

	private GeneralitemsCache() {
	}

	public static GeneralitemsCache getInstance() {
		if (instance == null)
			instance = new GeneralitemsCache();
		return instance;

	}
	
	public GeneralItemList getGeneralitems(Long gameId, Object... args) {
		String cacheKey = generateCacheKey(GENERALITEM_PREFIX, gameId, args); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (GeneralItemList) getCache().get(generateCacheKey(GENERALITEM_PREFIX, gameId, args));
	}
	
	public void putGeneralItemList(GeneralItemList giList, Long gameId, Object... args) {
		String cachekey = generateCacheKey(GENERALITEM_PREFIX, gameId, args); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(GENERALITEM_PREFIX, gameId, args), giList);
	}
	
	public ActionRelevancyPredictor getActionsPredicator(Long gameId) {
		String cacheKey = generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId); 
		if (!cacheKeyExists(gameId, cacheKey)) return null;
		return (ActionRelevancyPredictor) getCache().get(generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId));
	}
	
	public void putActionsPredicator(ActionRelevancyPredictor predictor, Long gameId) {
		String cachekey = generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId); 
		storeCacheKey(gameId, cachekey);
		getCache().put(generateCacheKey(ACTION_RELEVANCY_PREFIX, gameId), predictor);
	}
	
	public void removeGeneralItemList(Long gameId) {
		removeKeysForGame(gameId);
	}
	
	@Override
	protected String getType() {
		return "GeneralItems";
	}

}
