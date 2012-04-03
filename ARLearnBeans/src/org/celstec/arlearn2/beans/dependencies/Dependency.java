package org.celstec.arlearn2.beans.dependencies;

import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Action;

public abstract class Dependency extends Bean{

	private String type;

	public abstract long satisfiedAt(List<Action> actionList);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
