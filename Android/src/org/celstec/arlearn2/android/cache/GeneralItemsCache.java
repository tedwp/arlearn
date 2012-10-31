package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GeneralItemsCache extends GenericCache {

	private static GeneralItemsCache instance;

	private HashMap<Long, HashMap<Long, GeneralItem>> itemsForGameIdMap = new HashMap<Long, HashMap<Long, GeneralItem>>();
	private HashMap<Long, GeneralItem> itemsMap = new HashMap<Long, GeneralItem>();
	
//	private Set<Long> loadedRuns = new HashSet<Long>();

	private GeneralItemsCache() {

	}
//	public boolean runLoaded(Long runId) {
//		return loadedRuns.contains(runId);
//	}
	public static GeneralItemsCache getInstance() {
		if (instance == null) {
			instance = new GeneralItemsCache();
		}
		return instance;
	}

	public HashMap<Long, GeneralItem> getGeneralItemsWithGameId(Long gameId) {
		if (gameId == null) return new HashMap<Long, GeneralItem>();
		HashMap<Long, GeneralItem> result = itemsForGameIdMap.get(gameId);
		if (result == null) return new HashMap<Long, GeneralItem>();
		return result;
	}

	public GeneralItem getGeneralItems(long itemId) {
		return itemsMap.get(itemId);
	}

	public void flushCache(Long gameId) {
		itemsMap.remove(gameId);
		// todo remove from itemsMap
	}

	
	
	public void put(GeneralItem gi) {
		itemsMap.put(gi.getId(), gi);
		if (itemsForGameIdMap.get(gi.getGameId())== null) {
			itemsForGameIdMap.put(gi.getGameId(),  new HashMap<Long, GeneralItem>());
		}
		itemsForGameIdMap.get(gi.getGameId()).put(gi.getId(), gi);
	}
	
//	public void setRunLoaded(long runId) {
//		loadedRuns.add(runId);		
//	}
	
//	public void reloadFromDb(final long runId, final Context ctx) { //TODO query by runId/gameId?
//		DBAdapter.getAdapter(ctx).getGeneralItemAdapter().queryAll(new GeneralItemResults() {
//			
//			@Override
//			public void onResults(GeneralItem[] gis) {
//				loadedRuns.add(runId);
//				for (GeneralItem gi: gis) {
//					put(gi);
//				}
//				updateActivities(ctx, ListMessagesActivity.class.getCanonicalName(),
//						MapViewActivity.class.getCanonicalName(),
//						ListMapItemsActivity.class.getCanonicalName(),
//						NarratorItemActivity.class.getCanonicalName()
//						);
//			}
//		}) ;
//		
//	}
}
