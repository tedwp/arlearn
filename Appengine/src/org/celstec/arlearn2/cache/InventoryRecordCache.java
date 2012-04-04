package org.celstec.arlearn2.cache;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.run.InventoryRecord;

public class InventoryRecordCache extends RunCache{

	private static InventoryRecordCache instance;

	private static String INVENTORY_RECORD_PREFIX = "InventoryRecord";

	private InventoryRecordCache() {
	}

	public static InventoryRecordCache getInstance() {
		if (instance == null)
			instance = new InventoryRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "InventoryRecordCache";
	}

	public List<InventoryRecord> getInventoryRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<InventoryRecord>) getCache().get(cacheKey);
	}

	public void putInventoryRecordList(List<InventoryRecord> inventoryRecord, Long runId, String scope,  Object... args) {
		String cacheKey = generateCacheKey(INVENTORY_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, inventoryRecord);
	}
	
	public void removeInventoryRecords(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	public void putInventoryRecordList(InventoryRecord ir, Long runId, String scope, Long generalItemId, String email, String teamId) {
		ArrayList<InventoryRecord> irs = new ArrayList<InventoryRecord>();
		irs.add(ir);
		putInventoryRecordList(irs, runId, scope, generalItemId, email, teamId);
		
	}
}
