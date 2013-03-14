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
package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.run.Run;

@SuppressWarnings("serial")
public class RunModification extends NotificationBean{
	
	public final static int CREATED = RUN_CREATED;
	public final static int DELETED = RUN_DELETED;
	public final static int ALTERED = RUN_ALTERED;
	
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
			nullSafeEquals(getModificationType(), other.getModificationType()); 
	}
	
	public void retainOnlyIdentifier() {
		if (getRun() != null) {
			Run r = getRun();
			setRun(new Run());
			if (r.getRunId()!= null) getRun().setRunId(r.getRunId());
			if (r.getDeleted()!= null) getRun().setDeleted(r.getDeleted());
		}
	}
}
