package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import org.celstec.arlearn2.beans.run.Run;

public class RunCache extends GenericCache {
	private static RunCache instance;
	
	private HashMap<Long, Long> runToGameId = new HashMap<Long, Long>();
	private TreeSet<Run> allRuns = new TreeSet<Run>();
	
	private RunCache() {
	}

	public static RunCache getInstance() {
		if (instance == null) {
			instance = new RunCache();
		}
		return instance;
	}
	
	public Long getGameId(long runId) {
		return runToGameId.get(runId);
	}
	
	public Run[] getRuns() {
		return allRuns.toArray(new Run[]{});
	}
	
	public void put(Run r) {
		delete(r.getRunId());
		runToGameId.put(r.getRunId(), r.getGameId());
		if (r.getDeleted()!= null && !r.getDeleted()) allRuns.add(r);
	}
	
	public Run getRun(Long runId) {
		for (Run run: allRuns) {
			if (run.getRunId() == runId) return run;
		}
		return null;
	}

	public void delete(Long runId) {
		Run toDelete = null;
		for (Iterator<Run> iterator = allRuns.iterator(); iterator.hasNext();) {
			Run nextRun = iterator.next();
			if (nextRun.getRunId().equals(runId))  toDelete = nextRun;
		}
		if (toDelete != null) allRuns.remove(toDelete);
	}
	
}
