package org.celstec.arlearn2.cache;

import java.util.List;

import org.celstec.arlearn2.beans.run.ProgressRecord;

public class ProgressRecordCache extends RunCache{

	private static ProgressRecordCache instance;

	private static String PROGRESS_RECORD_PREFIX = "ProgressRecord";

	private ProgressRecordCache() {
	}

	public static ProgressRecordCache getInstance() {
		if (instance == null)
			instance = new ProgressRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "ProgressRecordCache";
	}
	
	public List<ProgressRecord> getProgressRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(PROGRESS_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<ProgressRecord>) getCache().get(cacheKey);
	}
	
	public void putProgressRecordList(List<ProgressRecord> pr, Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(PROGRESS_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeProgress(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	
}
