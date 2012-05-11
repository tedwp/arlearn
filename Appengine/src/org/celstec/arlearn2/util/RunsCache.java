package org.celstec.arlearn2.util;

import java.util.List;

import org.celstec.arlearn2.beans.run.Run;

import net.sf.jsr107cache.Cache;

public class RunsCache {

	private static RunsCache instance;
	private Cache cache;

	private RunsCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static RunsCache getInstance() {
		if (instance == null)
			instance = new RunsCache();
		return instance;
	}
	
	private static final String MYRUNS_PREFIX = "myRuns";
	
	public List<Long> getRunTableIds(String authToken) {
		List<Long> returnLong = (List<Long>) cache.get(MYRUNS_PREFIX+authToken);
		return returnLong;
	}
	
	public void putRunTableIds(String authToken, List<Long> ids) {
		cache.put(MYRUNS_PREFIX+authToken, ids);
	}
	
	public Run getRun(Long runId) {
		return (Run)cache.get(MYRUNS_PREFIX+runId);
	}
	
	public void putRun(Long runId, Run run) {
		cache.put(MYRUNS_PREFIX+runId, run);
	}
	
	
	public void removeRun(Long runId) {
		cache.remove(MYRUNS_PREFIX+runId);
	}
	
}