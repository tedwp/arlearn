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
