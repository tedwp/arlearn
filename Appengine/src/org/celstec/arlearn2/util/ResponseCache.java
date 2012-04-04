package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

public class ResponseCache {

	private static ResponseCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ResponseCache.class
			.getName());

	private ResponseCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ResponseCache getInstance() {
		if (instance == null)
			instance = new ResponseCache();
		return instance;
	}
	
	private static String RESPONSE_PREFIX = "responseprefix";

	public long getResponseTableIdForRun(Long runId) {
		Long responseLong = (Long) cache.get(RESPONSE_PREFIX+runId);
		if (responseLong == null) responseLong = -1l;
		return responseLong;
	}
	
	public void putResponseTableIdForRun(Long runId, Long tableId) {
		cache.put(RESPONSE_PREFIX+runId, tableId);
	}
}
