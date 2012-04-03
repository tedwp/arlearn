package org.celstec.arlearn2.beans.dependencies;

import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

public class TimeDependency extends Dependency{
	
	private Dependency offset;
	private Long timeDelta;
	
	public TimeDependency() {
		setType(TimeDependency.class.getName());
	}
	
	public Dependency getOffset() {
		return offset;
	}
	public void setOffset(Dependency offset) {
		this.offset = offset;
	}
	public Long getTimeDelta() {
		return timeDelta;
	}
	public void setTimeDelta(Long timeDelta) {
		this.timeDelta = timeDelta;
	}
	
	@Override
	public long satisfiedAt(List<Action> actionList) {
		if (offset == null) return -1;
		long satisfiedAt = offset.satisfiedAt(actionList);
		System.out.println("satisf at "+satisfiedAt);
		System.out.println("timede at "+timeDelta);
		System.out.println("+ at "+(satisfiedAt + timeDelta));
		if (satisfiedAt == -1) return -1;
		
		return  satisfiedAt + timeDelta;
	}
	
	public long satisfiedAtNoD(List<Action> actionList) {
		if (offset == null) return -1;
		long satisfiedAt = offset.satisfiedAt(actionList);
		if (satisfiedAt == -1) return -1;
		return  satisfiedAt;
	}

}
