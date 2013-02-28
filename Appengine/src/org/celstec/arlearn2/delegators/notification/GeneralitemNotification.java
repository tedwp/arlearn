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
package org.celstec.arlearn2.delegators.notification;

import java.util.HashMap;

import org.celstec.arlearn2.tasks.beans.NotifyItemVisible;

public class GeneralitemNotification extends Notification {
	public static final String ITEMID = "ITEMID";

	public GeneralitemNotification(int scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}
	
	public GeneralitemNotification(String scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}
	
	public void itemDeleted(String itemId) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "deleted");
		hm.put("itemId", itemId);
		notify("GeneralItem", hm);
	}
	
	
	public void itemVisible(NotifyItemVisible niv) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "visible");
		hm.put("itemId", ""+niv.getGeneralItemId());
		hm.put("name", niv.getName());
		if (niv.getLat()!=null) hm.put("lat", ""+niv.getLat());
		if (niv.getLng()!=null) hm.put("lng", ""+niv.getLng());
		hm.put("runId", ""+niv.getRunId());
		notify("GeneralItem", hm);
	}
	
}
