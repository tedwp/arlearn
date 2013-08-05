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
package org.celstec.arlearn2.beans.dependencies;

import java.util.List;
import java.util.Map;

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
	public long satisfiedAt(List<Action> actionList, Map<String, String[]> accountRoleMap) {
		if (offset == null) return -1;
		long satisfiedAt = offset.satisfiedAt(actionList, accountRoleMap);
		System.out.println("satisf at "+satisfiedAt);
		System.out.println("timede at "+timeDelta);
		System.out.println("+ at "+(satisfiedAt + timeDelta));
		if (satisfiedAt == -1) return -1;
		
		return  satisfiedAt + timeDelta;
	}
	
	private long satisfiedAtNoD(List<Action> actionList) {
		if (offset == null) return -1;
		long satisfiedAt = offset.satisfiedAt(actionList, null);
		if (satisfiedAt == -1) return -1;
		return  satisfiedAt;
	}

	@Override
	public boolean equals(Object obj) {
		TimeDependency other = (TimeDependency ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getOffset(), other.getOffset()) &&
			nullSafeEquals(getTimeDelta(), other.getTimeDelta())  ; 

	}
}
