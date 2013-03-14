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
import java.util.Map;
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

	public HashMap<Long, GeneralItem> getGeneralItemsButGameId(Long gameId) {
		if (gameId == null)
			return itemsMap;
		
		HashMap<Long, GeneralItem> hmAllGeneralItemsButGameId = new HashMap<Long, GeneralItem>();
		
		for (Map.Entry<Long, GeneralItem> entry : itemsMap.entrySet()) {
			
			GeneralItem value = entry.getValue();
			if(value.getGameId().longValue() != gameId.longValue()){
				hmAllGeneralItemsButGameId.put(entry.getKey(), value);
			}
		}
		
		return hmAllGeneralItemsButGameId;
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
	public void put(GeneralItem[] gis) {
		for (GeneralItem gi : gis) {
			put(gi);
		}
	}
	
	public void put(GeneralItem gi) {
		if (gi.getDeleted() !=null && gi.getDeleted()) {
			removeItem(gi);
			return;
		}
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
	
	public void removeItem(GeneralItem gi) {
		synchronized (itemsMap) {
			itemsMap.remove(gi.getId());
		}
		synchronized (itemsForGameIdMap) {
			HashMap<Long, GeneralItem> map = itemsForGameIdMap.get(gi.getGameId());
			if (map != null) {
				map.remove(gi.getId());
			}
		}
	}

	public Set<Long> getCachedGameIds() {
		return itemsForGameIdMap.keySet();
	}
	
}
