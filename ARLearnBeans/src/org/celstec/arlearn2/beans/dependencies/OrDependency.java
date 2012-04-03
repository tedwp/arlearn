package org.celstec.arlearn2.beans.dependencies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

public class OrDependency extends BooleanDependency {

	private List<Dependency> dependencies;

	public OrDependency() {
		setType(OrDependency.class.getName());
	}
	
	
	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public long satisfiedAt(List<Action> actionList) {
		long minTime = Long.MAX_VALUE;
		for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
			Dependency dep = (Dependency) iterator.next();
			long localMin = dep.satisfiedAt(actionList);
			if (localMin != -1) minTime = Math.min(minTime, localMin);
		}
		if (minTime == Long.MAX_VALUE) return -1;
		return minTime;
	}
	
	public void addDependecy(Dependency ad) {
		if (dependencies == null) dependencies = new ArrayList<Dependency>();
		dependencies.add(ad);
	}

}
