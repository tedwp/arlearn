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
	
	private HashMap<String, Integer> itemIdToStatus = new HashMap<String, Integer>();
	private Set<Long> loadedRuns = new HashSet<Long>();

	private GeneralItemVisibilityCache() {
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
	
	
	public TreeSet<GeneralItem> getAllItems(long runId) {
		if (itemIdToStatus == null) {
			return null;
		}
		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();
		GeneralItem old = null ;
		//TODO concurrent mod exception -> iterator
		for (Map.Entry<Long, GeneralItem> entry :GeneralItemsCache.getInstance().getGeneralItemsWithGameId(RunCache.getInstance().getGameId(runId)).entrySet()){
			Integer status = itemIdToStatus.get(runId+"*"+entry.getValue().getId());
			if (status == null && (entry.getValue().getDeleted() == null || !entry.getValue().getDeleted())) {
				put(runId, entry.getValue().getId(), GeneralItemVisibility.NOT_INITIALISED);
			} 
			resultList.add(entry.getValue());
		}
		return resultList;
	}
	
	public TreeSet<GeneralItem> getAllNotInitializedItems(long runId) {
		TreeSet<GeneralItem> resultList =getAllItems(runId);
		if (resultList == null) return null;
		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
			GeneralItem gi = iterator.next();
			if (itemIdToStatus.get(runId+"*"+gi.getId())!= GeneralItemVisibility.NOT_INITIALISED) {
				iterator.remove();
			}
		}
		return resultList;
	}
	
	public TreeSet<GeneralItem> getAllVisibleItems(long runId) {
		TreeSet<GeneralItem> resultList =getAllItems(runId);
		if (resultList == null) return null;
		for (Iterator<GeneralItem> iterator = resultList.iterator(); iterator.hasNext();) {
			GeneralItem gi = iterator.next();
			if (itemIdToStatus.get(runId+"*"+gi.getId())!= GeneralItemVisibility.VISIBLE) {
				iterator.remove();
			}
		}
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

	public void put(Long runId, long itemId, int status) {
		synchronized (itemIdToStatus) {
			if (runId == null) {
					for (Long runIdent: loadedRuns) {
						itemIdToStatus.put(runIdent+"*"+itemId, status);
					}
			} else {
				itemIdToStatus.put(runId+"*"+itemId, status);			
			}
		}
	}
	
	public void remove(long runId) {
		synchronized (itemIdToStatus) {
		for (String key: itemIdToStatus.keySet().toArray(new String[]{})) {
			if (key.startsWith(runId+"*")) {
				itemIdToStatus.remove(key);
			}
		}
		}
	}

	public boolean isVisible(long runId, Long id) {
		return (itemIdToStatus.get(runId+"*"+id)!= null && itemIdToStatus.get(runId+"*"+id) == GeneralItemVisibility.VISIBLE);
	}
}
