package org.celstec.arlearn2.beans.dependencies;

import java.util.List;

public abstract class BooleanDependency extends Dependency {

	
	public abstract List<Dependency> getDependencies();
	
	@Override
	public boolean equals(Object obj) {
		BooleanDependency other = (BooleanDependency ) obj;
		if (!super.equals(obj)) return false;
		List<Dependency> list1 = getDependencies();
		List<Dependency> list2 = other.getDependencies();
		if (!(list1.size() == list2.size())) return false;
		for (int i = 0 ; i< list1.size();i++) {
			if (!(list1.get(i).equals(list2.get(i)))) return false;
		}
		return true; 

	}
}
