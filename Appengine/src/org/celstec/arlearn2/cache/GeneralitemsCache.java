package org.celstec.arlearn2.cache;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

import com.google.appengine.api.utils.SystemProperty;

public class GeneralitemsCache extends GameCache {
	private static GeneralitemsCache instance;

	private static String GENERALITEM_PREFIX = SystemProperty.applicationVersion.get()+"GeneralItemDefinition";

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
	
	public void removeGeneralItemList(Long gameId) {
		removeKeysForGame(gameId);
	}
	
	@Override
	protected String getType() {
		return "GeneralItems";
	}

}
