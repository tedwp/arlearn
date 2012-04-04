package org.celstec.arlearn2.cache;

import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

import org.celstec.arlearn2.util.FusionCache;

public class LocationCache {

	private static LocationCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(LocationCache.class
			.getName());

	private LocationCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static LocationCache getInstance() {
		if (instance == null)
			instance = new LocationCache();
		return instance;
	}
	
	private static String LOCATIONS_PREFIX = "locationsPrefix";

	public long getLocationTableIdForRun(Long runId) {
		Long responseLong = (Long) cache.get(LOCATIONS_PREFIX+runId);
		if (responseLong == null) responseLong = -1l;
		return responseLong;
	}
	
	public void putLocationTableIdForRun(Long runId, Long tableId) {
		cache.put(LOCATIONS_PREFIX+runId, tableId);
	}
}
