package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.ScoreRecord;

import net.sf.jsr107cache.Cache;

public class ScoreRecordCache {

	private static ScoreRecordCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ScoreRecordCache.class
			.getName());

	private ScoreRecordCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ScoreRecordCache getInstance() {
		if (instance == null)
			instance = new ScoreRecordCache();
		return instance;
	}
	
	private static String SCORERECORD_PREFIX = "ScoreRecord";


	public int getScoreRecordTableIdForRun(String authToken, Long runId) {
		Integer returnInt = (Integer) cache.get(SCORERECORD_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putScoreDefinitionTableIdForRun(String authToken, Long runId, Integer tableId) {
		cache.put(SCORERECORD_PREFIX+authToken+runId, tableId);
	}
	
	
	public ScoreRecord getScoreRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope);
		} else if ("team".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope+teamId);
		} else if ("user".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope+email);
		}
		return null;
	}

	public void putScoreRecord(Long runId, ScoreRecord pr) {
		if (pr != null) {
			if ("all".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope(), pr);
			} else if ("team".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getTeamId(), pr);
			} else if ("user".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getEmail(), pr);
			}
		}
	}
	
	public void removeScoreRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope);
		} else if ("team".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope+email);
		}
	}
	
	public Long getScore(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+email);
		}
		return null;
	}

	public void putScore(Long runId, String scope, String email, String teamId, Long score) {
		if ("all".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+teamId, score);
		} else if ("team".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+teamId, score);
		} else if ("user".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+email, score);
		}
	}

	public void removeScore(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+email);
		}
	}



}
