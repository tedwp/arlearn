package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GeneralItemsCache extends GenericCache {

	private static GeneralItemsCache instance;

	private HashMap<Long, HashMap<Long, GeneralItem>> itemsForGameIdMap = new HashMap<Long, HashMap<Long, GeneralItem>>();
	private HashMap<Long, GeneralItem> itemsMap = new HashMap<Long, GeneralItem>();

	// private Set<Long> loadedRuns = new HashSet<Long>();

	private GeneralItemsCache() {

	}
	public void empty() {
		itemsForGameIdMap = new HashMap<Long, HashMap<Long, GeneralItem>>();
		itemsMap = new HashMap<Long, GeneralItem>();
	}

	// public boolean runLoaded(Long runId) {
	// return loadedRuns.contains(runId);
	// }
	public static GeneralItemsCache getInstance() {
		if (instance == null) {
			instance = new GeneralItemsCache();
		}
		return instance;
	}

	public HashMap<Long, GeneralItem> getGeneralItemsWithGameId(Long gameId) {
		if (gameId == null)
			return new HashMap<Long, GeneralItem>();
		HashMap<Long, GeneralItem> result = itemsForGameIdMap.get(gameId);
		if (result == null)
			return new HashMap<Long, GeneralItem>();
		return result;
	}

	public GeneralItem getGeneralItems(long itemId) {
		return itemsMap.get(itemId);
	}

	public void flushCache(Long gameId) {
		synchronized (itemsMap) {
			itemsMap.remove(gameId);
		}
		// todo remove from itemsMap
	}

	public void put(GeneralItem gi) {
		synchronized (itemsMap) {
			itemsMap.put(gi.getId(), gi);
		}
		synchronized (itemsForGameIdMap) {
			if (itemsForGameIdMap.get(gi.getGameId()) == null) {
				itemsForGameIdMap.put(gi.getGameId(), new HashMap<Long, GeneralItem>());
			}
			itemsForGameIdMap.get(gi.getGameId()).put(gi.getId(), gi);
		}
	}

	public Set<Long> getCachedGameIds() {
		return itemsForGameIdMap.keySet();
	}
}
