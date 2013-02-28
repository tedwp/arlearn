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
package org.celstec.arlearn2.delegators.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.beans.generalItem.PickupItem;
import org.celstec.arlearn2.beans.run.ProgressRecord;
import org.celstec.arlearn2.cache.ProgressRecordCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;
import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.jdo.manager.InventoryRecordManager;
import org.celstec.arlearn2.jdo.manager.ProgressRecordManager;
import org.celstec.arlearn2.cache.InventoryRecordCache;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class QueryInventoryRecord extends GoogleDelegator {

	public QueryInventoryRecord(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public QueryInventoryRecord(GoogleDelegator gd) {
		super(gd);
	}


	public InventoryRecord getInventoryRecord(Long runId, Long generalItemId, String scope, String email, String teamId) {
		if ("user".equals(scope)) {
			teamId = null;
		} else if ("team".equals(scope)) {
			email = null;
		} else if ("all".equals(scope)) {
			email = null;
			teamId = null;
		}
		List<InventoryRecord> list = getInventoryRecordList(runId, generalItemId, scope, email, teamId);
		if (list.isEmpty()) return null;
		return list.get(0);
	}
	
	public List<InventoryRecord> getInventoryRecordList(Long runId,  Long generalItemId,  String scope, String email, String teamId) {
		List<InventoryRecord> result = InventoryRecordCache.getInstance().getInventoryRecordList(runId, scope, generalItemId, email, teamId);
		if (result == null) {
			result = InventoryRecordManager.getInventoryRecord(runId, generalItemId, scope, email, teamId, null);
			InventoryRecordCache.getInstance().putInventoryRecordList(result, runId, scope, generalItemId,  email, teamId);
		}
		return result;
		
	}

}
