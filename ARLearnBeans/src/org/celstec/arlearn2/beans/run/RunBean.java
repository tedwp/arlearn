package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Game;

public class RunBean extends Bean{

	private Long runId;
	
	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public boolean equals(Object obj) {
		RunBean other = (RunBean ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getRunId(), other.getRunId()); 
	}
}