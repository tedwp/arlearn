package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GeneralItemsCache {

private static GeneralItemsCache instance;
	
	private HashMap<Long, GeneralItem[]> itemsMap = new HashMap<Long, GeneralItem[]>();
	
	private GeneralItemsCache() {
		
	}
	
	public static GeneralItemsCache getInstance() {
		if (instance == null) {
			instance = new GeneralItemsCache();
		}
		return instance;
	}

	public GeneralItem[] getGeneralItems(Long runId) {
		return itemsMap.get(runId);
	}
	
	public GeneralItem[] getGeneralItemsNotInitialized(Long runId) {
		
		//TODO
		return itemsMap.get(runId);
	}
	
	public void setActions(Long runId, GeneralItem[] items) {
		this.itemsMap.put(runId,  items);
	}
	
	public void flushCache(Long runId) {
		itemsMap.remove(runId);
	}
}
