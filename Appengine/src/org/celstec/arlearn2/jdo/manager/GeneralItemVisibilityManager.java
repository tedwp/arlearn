package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GeneralItemVisibilityJDO;

public class GeneralItemVisibilityManager {

	public static final int VISIBLE_STATUS = 1;
	public static final int DISAPPEARED_STATUS = 2;

	private static final String params[] = new String[] { "runId", "generalItemId", "email", "status" };
	private static final String paramsNames[] = new String[] { "runIdParam", "generalItemIdParam", "emailParam", "statusParam" };
	private static final String types[] = new String[] { "Long", "Long", "String", "Integer" };

	public static void setItemVisible(Long generalItemId, Long runId, String email, Integer status, long timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<GeneralItemVisibilityJDO> listVisiblity = getGeneralitemVisibility(pm, runId, generalItemId, email, status);
		try {
			if (listVisiblity.isEmpty()) {
				VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, email, status);
				GeneralItemVisibilityJDO visJdo = new GeneralItemVisibilityJDO();
				visJdo.setEmail(email);
				visJdo.setGeneralItemId(generalItemId);
				visJdo.setRunId(runId);
				visJdo.setStatus(status);
				visJdo.setTimeStamp(timeStamp);
				pm.makePersistent(visJdo);
			} else {
				for (GeneralItemVisibilityJDO vis : listVisiblity) {
					if (vis.getTimeStamp()== null || !vis.getTimeStamp().equals(timeStamp))  {
						vis.setTimeStamp(timeStamp);
						VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, email, status);
					}
				}
			}
		} finally {
			pm.close();
		}

	}

	public static HashMap<Long, Long> getVisibleItems(Long runId, String email) {
		return getItems(runId, email, VISIBLE_STATUS);
	}
	
	public static HashMap<Long,Long> getItems(Long runId, String email, int status) {
		HashMap<Long, Long> returnProgressDefinitions = new HashMap<Long, Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<GeneralItemVisibilityJDO> it = getGeneralitemVisibility(pm, runId, null, email, status).iterator();
		while (it.hasNext()) {
			GeneralItemVisibilityJDO gi = ((GeneralItemVisibilityJDO) it.next());
			returnProgressDefinitions.put(gi.getGeneralItemId(), gi.getTimeStamp());
		}
		return returnProgressDefinitions;
	}

	private static List<GeneralItemVisibilityJDO> getGeneralitemVisibility(PersistenceManager pm, Long runId, Long generalItemId, String email, Integer status) {
		Query query = pm.newQuery(GeneralItemVisibilityJDO.class);
		Object args[] = { runId, generalItemId, email, status };
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<GeneralItemVisibilityJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}

	public static void delete(Long runId, String email) {
		VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, email, GeneralItemVisibilityManager.VISIBLE_STATUS);
		VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, email, GeneralItemVisibilityManager.DISAPPEARED_STATUS);
		delete(runId, null, email, null);
	}

	protected static final Logger logger = Logger.getLogger(GeneralItemVisibilityManager.class.getName());

	public static void delete(Long runId, Long generalItemId, String email, Integer status) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<GeneralItemVisibilityJDO> deleteList = getGeneralitemVisibility(pm, runId, generalItemId, email, status);
			pm.deletePersistentAll(deleteList);
		} finally {
			pm.close();
		}
	}

}
