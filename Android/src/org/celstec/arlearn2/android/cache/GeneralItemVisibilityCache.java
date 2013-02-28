/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
	private HashSet<String> notyetvisibleItems = new HashSet<String>();
	private HashMap<String, Long> visibleItems = new HashMap<String, Long>();
	private HashMap<String, Long> disappearedItems = new HashMap<String, Long>();
	
	private Set<Long> loadedRuns = new HashSet<Long>();

	private GeneralItemVisibilityCache() {
	}
	
	public void empty() {
		notyetvisibleItems = new HashSet<String>();
		notInitialisedItems = new HashSet<String>();

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
	//TODO remove corresponding data structure
//	public TreeSet<GeneralItem> getAllNotInitializedItems(long runId) {
//		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();
//		for (String key: notInitialisedItems) {
//			long id = keyToGeneralItemId(key);
//			GeneralItem gi = GeneralItemsCache.getInstance().getGeneralItems(id);
//			if (gi != null) resultList.add(gi);
//		}
//		return resultList;
//	}
	
	public TreeSet<GeneralItem> getAllNotYetVisible(long runId) {
		TreeSet<GeneralItem> resultList = new TreeSet<GeneralItem>();
		for (String key: notyetvisibleItems) {
			long id = keyToGeneralItemId(key);
			GeneralItem gi = GeneralItemsCache.getInstance().getGeneralItems(id);
			if (gi != null) resultList.add(gi);
		}
		return resultList;
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
	
	private  long keyToGeneralItemId(String key) {
		if (!key.contains("*")) return -1;
		String id = key.substring(key.indexOf("*")+1);
		return Long.parseLong(id);
	}
	

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

	public void put(Long runId, long itemId, int status, Long satisfiedAt) {
		switch (status) {
		case GeneralItemVisibility.NOT_INITIALISED:
			synchronized (notInitialisedItems) {
				notInitialisedItems.add(getKey(runId, itemId));
			}
			break;
		case GeneralItemVisibility.NOT_YET_VISIBLE:
			synchronized (notInitialisedItems) {
				notInitialisedItems.remove(getKey(runId, itemId));
			}
			synchronized (notyetvisibleItems) {
				
				notyetvisibleItems.add(getKey(runId, itemId));
			}
			break;
		case GeneralItemVisibility.VISIBLE:
			synchronized (notInitialisedItems) {
				notInitialisedItems.remove(getKey(runId, itemId));
			}
			synchronized (notyetvisibleItems) {
				notyetvisibleItems.remove(getKey(runId, itemId));
			}
			synchronized (visibleItems) {
//				01-30 17:30:18.713: I/System.out(24406): before making visible 323001*309004 1359563406250
				if ("323001*319001".equals(getKey(runId, itemId))) {
					System.out.println("before making visible "+getKey(runId, itemId)+ " "+visibleItems.get(getKey(323001l, 318001l)));	
				}
				visibleItems.put(getKey(runId, itemId), satisfiedAt);
				if ("323001*319001".equals(getKey(runId, itemId))) {
					System.out.println("after making visible "+getKey(runId, itemId)+ " "+visibleItems.get(getKey(323001l, 318001l)));
				}
				
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
		remove(notInitialisedItems, runId);
		remove(notyetvisibleItems, runId);
		
	}
	
	//TODO test this method
	public void removeWithGenItemId(Long itemId) {
		removeWithGenItemId(visibleItems, itemId);
		removeWithGenItemId(disappearedItems, itemId);
		removeWithGenItemId(notInitialisedItems, itemId);
		removeWithGenItemId(notyetvisibleItems, itemId);
	}
	
	public void removeWithGenItemId(HashMap<String, Long> map, Long itemId) {
		synchronized (map) {
			Iterator<String> runItems = map.keySet().iterator();
			Set<String> keysToRemove = new HashSet<String>();
			while (runItems.hasNext()) {
				String key = runItems.next();
				if (keyContainsItemId(key, itemId)){
					keysToRemove.add(key);
				}
			}
			for (String key: keysToRemove){
				map.remove(key);
			}
		}
	}
		
	public void removeWithGenItemId(HashSet<String> set, Long itemId) {
		synchronized (set) {
			Iterator<String> runItems = set.iterator();
			while (runItems.hasNext()) {
				if (keyContainsItemId(runItems.next(), itemId)){
					runItems.remove();
				}
			}
		}
	}
	
	

	
	private void remove(HashMap<String, Long> hm, long runId) {
		synchronized (hm) {
			for (String key : hm.keySet().toArray(new String[] {})) {
				if (key.startsWith(runId + "*")) {
					hm.remove(key);
				}
			}
		}
	}
	
	private void remove(HashSet<String> hs, long runId) {
		synchronized (hs) {
			for (Iterator<String> iterator = hs.iterator(); iterator.hasNext();) {
				if (iterator.next().startsWith(runId + "*")) {
					iterator.remove();
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
	
	public boolean keyContainsItemId(String key, long itemId) {
		return key.endsWith("*"+itemId);
	}

}
