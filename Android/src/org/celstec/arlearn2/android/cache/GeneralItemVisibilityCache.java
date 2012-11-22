package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.content.Context;

public class GeneralItemVisibilityCache {

	private static GeneralItemVisibilityCache instance;
	
	private HashSet<String> notInitialisedItems = new HashSet<String>();
	private HashMap<String, Long> visibleItems = new HashMap<String, Long>();
	private HashMap<String, Long> disappearedItems = new HashMap<String, Long>();
	
//	private HashMap<String, Integer> itemIdToStatus = new HashMap<String, Integer>();
	private Set<Long> loadedRuns = new HashSet<Long>();

	private GeneralItemVisibilityCache() {
	}
	
	public void empty() {
//		itemIdToStatus = new HashMap<String, Integer>();
		visibleItems = new HashMap<String, Long>();
		disappearedItems = new HashMap<String, Long>();
		
		loadedRuns = new HashSet<Long>();

	}

	public boolean runLoaded(Long runId) {
		return loadedRuns.contains(runId);
	}
	public static GeneralItemVisibilityCache getInstance() {
		if (instance == null) {
			instance = new GeneralItemVisibilityCache();
		}
		return instance;
	}
	
	
//	public TreeSet<GeneralItem> getAllItems(long runId) {
//		long now = System.currentTimeMillis();
//		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();
//
//		for (Map.Entry<Long, GeneralItem> entry :GeneralItemsCache.getInstance().getGeneralItemsWithGameId(RunCache.getInstance().getGameId(runId)).entrySet()){
//			Long appearAt = visibleItems.get(getKey(runId, entry.getValue().getId()));
//			Long disappearAt = disappearedItems.get(getKey(runId, entry.getValue().getId()));
//			if (appearAt != null) {
//				if (appearAt < now) {
//					if (disappearAt == null || disappearAt > now) {
//						resultList.add(entry.getValue());
//					}
//				}
//			}
//		}

//		if (itemIdToStatus == null) {
//			return null;
//		}
//		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();
//		GeneralItem old = null ;
//		//TODO concurrent mod exception -> iterator
//		for (Map.Entry<Long, GeneralItem> entry :GeneralItemsCache.getInstance().getGeneralItemsWithGameId(RunCache.getInstance().getGameId(runId)).entrySet()){
//			Integer status = itemIdToStatus.get(runId+"*"+entry.getValue().getId());
//			if (status == null && (entry.getValue().getDeleted() == null || !entry.getValue().getDeleted())) {
//				put(runId, entry.getValue().getId(), GeneralItemVisibility.NOT_INITIALISED, -1l);
//			} 
//			resultList.add(entry.getValue());
//		}
//		return resultList;
//	}
	
//	public TreeSet<GeneralItem> getAllNotInitializedItems(long runId) {
//		TreeSet<GeneralItem> resultList =getAllItems(runId);
//		if (resultList == null) return null;
//		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
//			GeneralItem gi = iterator.next();
//			if (itemIdToStatus.get(runId+"*"+gi.getId()) == null || itemIdToStatus.get(runId+"*"+gi.getId())!= GeneralItemVisibility.NOT_INITIALISED) {
//				iterator.remove();
//			}
//		}
//		return resultList;
//	}
	
	public TreeSet<GeneralItem> getAllVisibleItems(long runId) {
		long now = System.currentTimeMillis();
		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();

		for (Map.Entry<Long, GeneralItem> entry :GeneralItemsCache.getInstance().getGeneralItemsWithGameId(RunCache.getInstance().getGameId(runId)).entrySet()){
			Long appearAt = visibleItems.get(getKey(runId, entry.getValue().getId()));
			Long disappearAt = disappearedItems.get(getKey(runId, entry.getValue().getId()));
			if (appearAt != null) {
				if (appearAt < now) {
					if (disappearAt == null || disappearAt > now) {
						resultList.add(entry.getValue());
					}
				}
			}
		}
//		TreeSet<GeneralItem> resultList =getAllItems(runId);
//		if (resultList == null) return null;
//		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
//			GeneralItem gi = iterator.next();
//			if (itemIdToStatus.get(runId+"*"+gi.getId()) == null ||itemIdToStatus.get(runId+"*"+gi.getId())!= GeneralItemVisibility.VISIBLE) {
//				iterator.remove();
//			}
//		}
		return resultList;
	}
	
	public TreeSet<GeneralItem> getAllVisibleMessages(long runId) {
		TreeSet<GeneralItem> resultList =getAllVisibleItems(runId);
		if (resultList == null) return null;
		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
			GeneralItem gi = iterator.next();
			if (gi.getLat() != null) {
				iterator.remove();
			}
		}
		return resultList;
	}
	
	public TreeSet<GeneralItem> getAllVisibleLocations(long runId) {
		TreeSet<GeneralItem> resultList =getAllVisibleItems(runId);
		if (resultList == null) return null;
		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
			GeneralItem gi = iterator.next();
			if (gi.getLat() == null) {
				iterator.remove();
			}
		}
		return resultList;
	}
	
	public void setRunLoaded(long runId) {
		loadedRuns.add(runId);		
	}

	public void put(Long runId, long itemId, int status, long satisfiedAt) {
		switch (status) {
		case GeneralItemVisibility.VISIBLE:
			synchronized (visibleItems) {
				visibleItems.put(getKey(runId, itemId), satisfiedAt);
			}
			break;
		case GeneralItemVisibility.NO_LONGER_VISIBLE:
			synchronized (disappearedItems) {
				disappearedItems.put(getKey(runId, itemId), satisfiedAt);
			}
			break;
		default:
			break;
		}
//		synchronized (itemIdToStatus) {
//			if (runId == null) {
//				for (Long runIdent : loadedRuns) {
//					itemIdToStatus.put(runIdent + "*" + itemId, status);
//				}
//			} else {
//				itemIdToStatus.put(runId + "*" + itemId, status);
//			}
//		}
	}
	
	public void remove(long runId) {
		remove(visibleItems, runId);
		remove(disappearedItems, runId);
		synchronized (notInitialisedItems) {
			for (Iterator<String> iterator = notInitialisedItems.iterator(); iterator.hasNext();) {
				if (iterator.next().startsWith(runId + "*")) {
					iterator.remove();
				}

			}
		}
	}
	
	public void remove(HashMap<String, Long> hm, long runId) {
		synchronized (hm) {
			for (String key : hm.keySet().toArray(new String[] {})) {
				if (key.startsWith(runId + "*")) {
					hm.remove(key);
				}
			}
		}
	}

	public boolean isVisible(long runId, Long id) {
		return visibleItems.get(getKey(runId, id)) != null && visibleItems.get(getKey(runId, id)) < System.currentTimeMillis();
//		return (itemIdToStatus.get(runId+"*"+id)!= null && itemIdToStatus.get(runId+"*"+id) == GeneralItemVisibility.VISIBLE);
	}
	
	public long disappearedAt(long runId, long itemId) {
		if (disappearedItems.containsKey(getKey(runId, itemId))) {
			return disappearedItems.get(getKey(runId, itemId));
		}
		return -1;
	}
	
	public String getKey(Long runId, long itemId) {
		return runId+"*"+itemId;
	}
}
