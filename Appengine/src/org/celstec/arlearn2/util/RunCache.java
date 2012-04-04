package org.celstec.arlearn2.util;

import java.util.HashSet;
import net.sf.jsr107cache.Cache;

public abstract class RunCache {
	
	private static String PROGRESSRECORD_CK_PREFIX = "RunCache:CK";

	protected abstract Cache getCache();
	protected abstract String getType();

	protected void storeCacheKey(Long runId, String cachekey) {
		HashSet<String> hs = getCacheKey(runId);
		if (hs == null) {
			hs = new HashSet<String>();
		}  
		if (!hs.contains(cachekey)) {
			hs.add(cachekey);
			getCache().put(PROGRESSRECORD_CK_PREFIX+getType()+runId, hs);
		}
		System.out.println("storing cacheKey "+hs);
	}
	
	private HashSet<String> getCacheKey(Long runId) {
		return (HashSet<String>) getCache().get(PROGRESSRECORD_CK_PREFIX+getType()+runId);
	}
	
	protected boolean cacheKeyExists(Long runId, String cacheKey) {
		HashSet<String> hs = getCacheKey(runId);
		if (hs == null) return false;
		return hs.contains(cacheKey);
	}
	
	public void removeKeysForRun(Long runId) {
		 getCache().remove(PROGRESSRECORD_CK_PREFIX+getType()+runId);
	}
}
