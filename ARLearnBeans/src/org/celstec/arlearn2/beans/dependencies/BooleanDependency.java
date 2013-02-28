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

public abstract class BooleanDependency extends Dependency {

	
	public abstract List<Dependency> getDependencies();
	
	@Override
	public boolean equals(Object obj) {
		BooleanDependency other = (BooleanDependency ) obj;
		if (!super.equals(obj)) return false;
		List<Dependency> list1 = getDependencies();
		List<Dependency> list2 = other.getDependencies();
		if (!(list1.size() == list2.size())) return false;
		for (int i = 0 ; i< list1.size();i++) {
			if (!(list1.get(i).equals(list2.get(i)))) return false;
		}
		return true; 

	}
}
