/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
