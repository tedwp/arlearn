package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;

public class RunConfig extends Bean {
	
	private Boolean selfRegistration;

	public Boolean getSelfRegistration() {
		return selfRegistration;
	}

	public void setSelfRegistration(Boolean selfRegistration) {
		this.selfRegistration = selfRegistration;
	}
	
	@Override
	public boolean equals(Object obj) {
		RunConfig other = (RunConfig ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getSelfRegistration(), other.getSelfRegistration()) ; 

	}

}
