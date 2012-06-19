package org.celstec.arlearn2.cache;

import java.util.List;

import org.celstec.arlearn2.beans.run.VisibleItem;

import com.google.appengine.api.utils.SystemProperty;

public class VisibleItemsCache extends RunCache{

	private static VisibleItemsCache instance;

	private static String INVENTORY_RECORD_PREFIX = SystemProperty.applicationVersion.get()+"InventoryRecord";

	private VisibleItemsCache() {
	}

	public static VisibleItemsCache getInstance() {
		if (instance == null)
			instance = new VisibleItemsCache();
		return instance;
	}

	public List<VisibleItem> getVisibleItems(Long runId, Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId,  args); 
		return (List<VisibleItem>) getCache().get(cacheKey);
	}

	public void putVisibleItems(List<VisibleItem> visibleItem, Long runId, Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, args); 
		getCache().put(cacheKey, visibleItem);
	}
	
	public void removeVisibleItems(Long runId, Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, args); 
		getCache().remove(cacheKey);
	}
	
	@Override
	protected String getType() {
		return "VisibleItemsCache";
	}

}
