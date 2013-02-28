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
package org.celstec.arlearn2.delegators;

import java.util.List;

import org.celstec.arlearn2.beans.run.GeneralItemVisibility;
import org.celstec.arlearn2.beans.run.GeneralItemVisibilityList;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;

import com.google.gdata.util.AuthenticationException;

public class GeneralItemVisibilityDelegator  extends GoogleDelegator {

	public GeneralItemVisibilityDelegator(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public GeneralItemVisibilityDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public GeneralItemVisibilityList getVisibleItems(Long runId, String email, Long from, Long until) {
		if (from == null && until == null) {
			from = 0l;
		}
		GeneralItemVisibilityList list = new GeneralItemVisibilityList();
		list.setGeneralItemsVisibility( GeneralItemVisibilityManager.getGeneralitemsFromUntil(runId, email, from, until));
		list.setServerTime(System.currentTimeMillis());
		return list;
	}


}
