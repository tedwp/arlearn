package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunBean;

public class RunModification extends Bean{
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	private Integer modificationType;
	
	private Run run;
	
	public RunModification(){}

	public Integer getModificationType() {
		return modificationType;
	}

	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	@Override
	public boolean equals(Object obj) {
		RunModification other = (RunModification ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getRun(), other.getRun()) && 
			nullSafeEquals(getRun(), other.getRun()); 
	}
}
