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
package org.celstec.arlearn2.beans.dependencies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	public long satisfiedAt(List<Action> actionList, Map<String, String[]> accountRoleMap) {
		long maxTime = 0;
		for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
			Dependency dep = (Dependency) iterator.next();
			long localMax = dep.satisfiedAt(actionList, accountRoleMap);
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
