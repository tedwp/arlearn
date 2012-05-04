package org.celstec.arlearn2.beans.dependencies;

import java.util.List;

public abstract class BooleanDependency extends Dependency {

	
	public abstract List<Dependency> getDependencies();
}
