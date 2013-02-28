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
import java.util.Iterator;
import java.util.TreeSet;
import org.celstec.arlearn2.beans.run.Run;

public class RunCache extends GenericCache {
	private static RunCache instance;

	private HashMap<Long, Long> runToGameId = new HashMap<Long, Long>();
	private TreeSet<Run> allRuns = new TreeSet<Run>();

	private RunCache() {
	}

	public void empty() {
		runToGameId = new HashMap<Long, Long>();
		allRuns = new TreeSet<Run>();
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
		synchronized (allRuns) {
			return allRuns.toArray(new Run[] {});
		}
	}
	
	public void put(Run r) {
		delete(r.getRunId());
		synchronized (runToGameId) {
			runToGameId.put(r.getRunId(), r.getGameId());
		}

		if (r.getDeleted() != null && !r.getDeleted()) {
			synchronized (allRuns) {
				allRuns.add(r);
			}
		}
	}
	
	public Run getRun(Long runId) {
		for (Run run: allRuns) {
			if (run.getRunId().equals(runId)) return run;
		}
		return null;
	}

	public void delete(Long runId) {
		Run toDelete = null;
		for (Iterator<Run> iterator = allRuns.iterator(); iterator.hasNext();) {
			Run nextRun = iterator.next();
			if (nextRun.getRunId().equals(runId))  toDelete = nextRun;
		}
		synchronized (allRuns) {
			if (toDelete != null) allRuns.remove(toDelete);	
		}
	}
	
}
