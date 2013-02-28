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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.celstec.arlearn2.beans.run.Action;

public class ActionCache {
	
	private static ActionCache instance;
	
	private HashMap<Long, List<Action>> actionsMap = new HashMap<Long, List<Action>>();
	private HashMap<Long, HashMap<Long, Action>> readMap = new HashMap<Long, HashMap<Long, Action>>();
	private Set<Long> loadedRuns = new HashSet<Long>();

	private ActionCache() {
		
	}
	
	public static ActionCache getInstance() {
		if (instance == null) {
			instance = new ActionCache();
		}
		return instance;
	}
	
	public boolean runLoaded(Long runId) {
		return loadedRuns.contains(runId);
	}
	
	public boolean isRead(long runId, long itemId) {
		if (!readMap.containsKey(runId)) return false;
		if (!readMap.get(runId).containsKey(itemId)) return false;
		return true; 
	}

	public List<Action> getActions(Long runId) {
		return actionsMap.get(runId);
	}
	
//	public void setActions(Long runId, List<Action> actions) {
//		this.actionsMap.put(runId,  actions);
//	}
	
	public void flushCache(Long runId) {
		synchronized (actionsMap) {
			actionsMap.remove(runId);
		}
	}
	
	
	public void setRunLoaded(long runId) {
		synchronized (loadedRuns) {
			loadedRuns.add(runId);		
		}
	}
	
	public void cacheAction(long runId, Action a) {
		synchronized (actionsMap) {
			synchronized (readMap) {
				if (actionsMap.get(runId) == null)
					actionsMap.put(runId, new ArrayList<Action>());
				if (readMap.get(runId) == null)
					readMap.put(runId, new HashMap<Long, Action>());
				actionsMap.get(runId).add(a);
				if ("read".equals(a.getAction()) && a.getGeneralItemId() != null) {
					readMap.get(runId).put(a.getGeneralItemId(), a);
				}
			}
		}
	}

	public void delete(Long runId) {
		synchronized (actionsMap) {
			loadedRuns.remove(runId);
		}
		synchronized (actionsMap) {

			actionsMap.remove(runId);
		}
		synchronized (readMap) {
			readMap.remove(runId);
		}

	}

	public void empty() {
		actionsMap = new HashMap<Long, List<Action>>();
		readMap = new HashMap<Long, HashMap<Long, Action>>();
		loadedRuns = new HashSet<Long>();
	}
	
}
