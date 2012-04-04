package org.celstec.arlearn2.cache;

import java.util.HashSet;

import net.sf.jsr107cache.Cache;

public abstract class GameCache extends GenericCache {

	private static String GAMECACHE_CK_PREFIX = "GameCache:CK";

	protected abstract String getType();

	protected void storeCacheKey(Long gameId, String cachekey) {
		HashSet<String> hs = getCacheKey(gameId);
		if (hs == null) {
			hs = new HashSet<String>();
		}  
		if (!hs.contains(cachekey)) {
			hs.add(cachekey);
			getCache().put(GAMECACHE_CK_PREFIX+getType()+gameId, hs);
		}
		System.out.println("storing game cacheKey "+hs);
	}
	
	private HashSet<String> getCacheKey(Long gameId) {
		return (HashSet<String>) getCache().get(GAMECACHE_CK_PREFIX+getType()+gameId);
	}
	
	protected boolean cacheKeyExists(Long gameId, String cacheKey) {
		HashSet<String> hs = getCacheKey(gameId);
		if (hs == null) return false;
		return hs.contains(cacheKey);
	}
	
	protected void removeKeysForGame(Long gameId) {
		 getCache().remove(GAMECACHE_CK_PREFIX+getType()+gameId);
	}
	
}
