package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GeneralItemVisibilityJDO;

public class GeneralItemVisibilityManager {
	
	public static final int VISIBLE_STATUS = 1;
	public static final int DISAPPEARED_STATUS = 2;

	private static final String params[] = new String[]{"runId", "generalItemId", "email" , "status"};
	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "emailParam", "statusParam"};
	private static final String types[] = new String[]{"Long","Long", "String", "Integer"};
	
	public static void setItemVisible(Long generalItemId, Long runId, String email, Integer status)  {
		VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, email);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		GeneralItemVisibilityJDO visJdo = new GeneralItemVisibilityJDO();
		visJdo.setEmail(email);
		visJdo.setGeneralItemId(generalItemId);
		visJdo.setRunId(runId);
		visJdo.setStatus(status);
		try {
			pm.makePersistent(visJdo);
		} finally {
			pm.close();
		}
	}
	
	public static List<Long> getVisibleItems(Long runId, String email) {
		ArrayList<Long> returnProgressDefinitions = new ArrayList<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<GeneralItemVisibilityJDO> it = getGeneralitemVisibility(pm, runId, null, email, VISIBLE_STATUS).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(((GeneralItemVisibilityJDO) it.next()).getGeneralItemId());
		}
		return returnProgressDefinitions;
	}
	
	private static List<GeneralItemVisibilityJDO> getGeneralitemVisibility(PersistenceManager pm, Long runId, Long generalItemId, String email, Integer status) {
		Query query = pm.newQuery(GeneralItemVisibilityJDO.class);
		Object args [] ={runId, generalItemId, email, status};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<GeneralItemVisibilityJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	public static void delete(Long runId, String email){
		delete(runId, null, email, null);
	}
	protected static final Logger logger = Logger.getLogger(GeneralItemVisibilityManager.class.getName());

	public static void delete(Long runId, Long generalItemId, String email, Integer status){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List< GeneralItemVisibilityJDO> deleteList = getGeneralitemVisibility(pm, runId, generalItemId, email, status);
			pm.deletePersistentAll(deleteList);
		} finally {
			pm.close();
		}
	}
	
}
