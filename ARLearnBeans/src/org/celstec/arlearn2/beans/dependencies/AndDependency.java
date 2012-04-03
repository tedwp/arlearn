package org.celstec.arlearn2.beans.dependencies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

public class AndDependency extends BooleanDependency {
	
	private List<Dependency> dependencies;

	public AndDependency() {
		setType(AndDependency.class.getName());
	}
	
	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public long satisfiedAt(List<Action> actionList) {
		long maxTime = 0;
		for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
			Dependency dep = (Dependency) iterator.next();
			long localMax = dep.satisfiedAt(actionList);
			if (localMax == -1) return localMax;
			maxTime = Math.max(maxTime, localMax);
		}
		return maxTime;
	}

	public void addDependecy(Dependency ad) {
		if (dependencies == null) dependencies = new ArrayList<Dependency>();
		dependencies.add(ad);
	}

}
