package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;

public class RunConfig extends Bean {
	
	private Boolean selfRegistration;
	private String nfcTag;

	public Boolean getSelfRegistration() {
		return selfRegistration;
	}

	public void setSelfRegistration(Boolean selfRegistration) {
		this.selfRegistration = selfRegistration;
	}
	
	public String getNfcTag() {
		return nfcTag;
	}

	public void setNfcTag(String nfcTag) {
		this.nfcTag = nfcTag;
	}

	@Override
	public boolean equals(Object obj) {
		RunConfig other = (RunConfig ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getNfcTag(), other.getNfcTag()) && 
			nullSafeEquals(getSelfRegistration(), other.getSelfRegistration()) ; 

	}

}
