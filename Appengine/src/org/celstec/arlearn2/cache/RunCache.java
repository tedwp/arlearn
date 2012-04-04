package org.celstec.arlearn2.cache;

import java.util.HashSet;

public abstract class RunCache extends GenericCache {

	private static String RUNCACHE_CK_PREFIX = "RunCache:CK";

	protected abstract String getType();

	protected void storeCacheKeyForScope(Long runId, String scope, String cachekey) {
		super.storeCacheKey(runId, RUNCACHE_CK_PREFIX+getType()+scope, cachekey);
	}
	
	protected HashSet<String> getCacheKeyForScope(Long runId, String scope) {
		return super.getCacheKey(runId, RUNCACHE_CK_PREFIX+getType()+scope);
	}
	
	protected boolean cacheKeyExistsForScope(Long runId, String scope, String cacheKey) {
		return super.cacheKeyExists(runId, RUNCACHE_CK_PREFIX+getType()+scope, cacheKey);		
	}
	
	protected void removeKeysForRun(Long runId, String scope) {
		super.removeKeysForGame(runId, RUNCACHE_CK_PREFIX+getType()+scope);
	}
}
