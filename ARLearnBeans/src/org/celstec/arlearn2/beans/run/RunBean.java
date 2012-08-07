package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Game;

public class RunBean extends Bean{

	private Long runId;
	private Boolean deleted;

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public Boolean getDeleted() {
		if (deleted == null) return false;
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object obj) {
		RunBean other = (RunBean ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getDeleted(), other.getDeleted()) && 
			nullSafeEquals(getRunId(), other.getRunId()); 
	}
}