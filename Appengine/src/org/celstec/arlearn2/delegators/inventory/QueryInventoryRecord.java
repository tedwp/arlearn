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
