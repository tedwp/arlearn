package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

public class ActionCache {
	
	private static ActionCache instance;
	
	private HashMap<Long, List<Action>> actionsMap = new HashMap<Long, List<Action>>();
	
	private ActionCache() {
		
	}
	
	public static ActionCache getInstance() {
		if (instance == null) {
			instance = new ActionCache();
		}
		return instance;
	}

	public List<Action> getActions(Long runId) {
		return actionsMap.get(runId);
	}
	
	public void setActions(Long runId, List<Action> actions) {
		this.actionsMap.put(runId,  actions);
	}
	
	public void flushCache(Long runId) {
		actionsMap.remove(runId);
	}
	
}
