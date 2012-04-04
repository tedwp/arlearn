package org.celstec.arlearn2.cache;

import java.util.List;

import org.celstec.arlearn2.beans.run.ScoreRecord;

public class ScoreRecordCache extends RunCache{

	private static ScoreRecordCache instance;

	private static String SCORE_RECORD_PREFIX = "ScoreRecord";

	private ScoreRecordCache() {
	}

	public static ScoreRecordCache getInstance() {
		if (instance == null)
			instance = new ScoreRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "ScoreRecordCache";
	}
	
	public List<ScoreRecord> getScoreRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(SCORE_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<ScoreRecord>) getCache().get(cacheKey);
	}
	
	public void putScoreRecordList(List<ScoreRecord> pr, Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(SCORE_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeScore(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	
}
