package org.celstec.arlearn2.beans.dependencies;

import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Action;

public abstract class Dependency extends Bean{

	public static final int USER_SCOPE = 0;
	public static final int TEAM_SCOPE = 1;
	public static final int ALL_SCOPE = 2;
	
	private String type;
	
	public abstract long satisfiedAt(List<Action> actionList);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
