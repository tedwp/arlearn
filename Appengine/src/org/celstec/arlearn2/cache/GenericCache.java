package org.celstec.arlearn2.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public class GenericCache {
	private static final Logger logger = Logger.getLogger(GenericCache.class.getName());

	private static Cache cache;

	protected GenericCache() {
		if (cache == null) {
			try {
				cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
			} catch (CacheException e) {
				logger.severe(e.getMessage());
			}
		}
	}
	
	protected Cache getCache() {
		return cache;
	}

	public static String generateCacheKey(java.lang.Object... parameters) {
		return generateCacheKeyAr(parameters);
	}
	
	private static String generateCacheKeyAr(Object parameters[]) {
		String key = "";
		for (Object p: parameters) {
			if (p == null) {
				key += ":null";
			} else {
				if (p instanceof Object[]) {
					key += generateCacheKeyAr((Object[])p);
				} else {
					key += ":"+p.toString();	
				}
					
			}
		}
		return key;
	}
	
	protected void storeCacheKey(Long id, String prefix, String cachekey) {
		HashSet<String> hs = getCacheKey(id, prefix);
		if (hs == null) {
			hs = new HashSet<String>();
		}  
		if (!hs.contains(cachekey)) {
			hs.add(cachekey);
			getCache().put(prefix+id, hs);
		}
		System.out.println("storing game cacheKey "+hs);
	}
	
	protected HashSet<String> getCacheKey(Long id, String prefix) {
		return (HashSet<String>) getCache().get(prefix+id);
	}
	
	protected boolean cacheKeyExists(Long id, String prefix, String cacheKey) {
		HashSet<String> hs = getCacheKey(id, prefix);
		if (hs == null) return false;
		return hs.contains(cacheKey);
	}
	
	protected void removeKeysForGame(Long id, String prefix) {
		 getCache().remove(prefix+id);
	}
	
}
