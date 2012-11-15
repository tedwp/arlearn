package org.celstec.arlearn2.cache;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

import com.google.appengine.api.utils.SystemProperty;

public class VisibleGeneralItemsCache extends GameCache {
	private static VisibleGeneralItemsCache instance;
	
	private static String VISIBLEGENERALITEM_PREFIX = SystemProperty.applicationVersion.get()+"VisibleGeneralItemDefinition";

	private VisibleGeneralItemsCache() {
	}

	public static VisibleGeneralItemsCache getInstance() {
		if (instance == null)
			instance = new VisibleGeneralItemsCache();
		return instance;
	}
	
	public GeneralItemList getVisibleGeneralitems(Long runId, Object... args) {
		String cacheKey = generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args); 
		if (!cacheKeyExists(runId, cacheKey)) return null;
		return (GeneralItemList) getCache().get(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args));
	}
	
	public void putVisibleGeneralItemList(GeneralItemList giList, Long runId, Object... args) {
		String cachekey = generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args); 
		storeCacheKey(runId, cachekey);
		getCache().put(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args), giList);
	}
	
	public void removeGeneralItemList(Long runId, Object... args) {
		 getCache().remove(generateCacheKey(VISIBLEGENERALITEM_PREFIX, runId, args));
	
	}
	
	public void removeGeneralItemList(Long runId) {
		removeKeysForGame(runId);
	}
	
	@Override
	protected String getType() {
		return "VisibleGeneralItems";
	}
	

}
