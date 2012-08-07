package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ActionJDO;

public class ActionManager {

	private static final String params[] = new String[]{"runId", "action", "userEmail", "generalItemId", "generalItemType"};
	private static final String paramsNames[] = new String[]{"runIdParam", "actionParam", "userEmailParam", "generalItemIdParam", "generalItemTypeParam"};
	private static final String types[] = new String[]{"Long", "String", "String", "String", "String"};
	
	public static List<Action> getActions(Long runId, String action, String userEmail, String generalItemId, String generalItemType) {
		ArrayList<Action> returnProgressDefinitions = new ArrayList<Action>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<ActionJDO> it = getActionsJDO(pm, runId,action, userEmail, generalItemId, generalItemType).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((ActionJDO) it.next()));
		}
		return returnProgressDefinitions;

	}
	
	private static List<ActionJDO> getActionsJDO(PersistenceManager pm, Long runId, String action, String userEmail, String generalItemId, String generalItemType) {
		Query query = pm.newQuery(ActionJDO.class);
		Object args [] ={runId,action, userEmail, generalItemId, generalItemType};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<ActionJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	
	public static void addAction(Long runId, String action, String userEmail, Long generalItemId, String generalItemType, Long time) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActionJDO actionJDO = new ActionJDO();
		actionJDO.setAction(action);
		actionJDO.setGeneralItemId(generalItemId);
		actionJDO.setGeneralItemType(generalItemType);
		actionJDO.setRunId(runId);
		actionJDO.setTime(time);
		actionJDO.setUserEmail(userEmail);
		try {
			pm.makePersistent(actionJDO);
		} finally {
			pm.close();
		}
	}

	public static ActionList runActions(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<ActionJDO> it = runActions(pm, runId).iterator();
			ActionList returnList = new ActionList();
			returnList.setRunId(runId);
			while (it.hasNext()) {
				returnList.addAction(toBean((ActionJDO) it.next()));
			}
			return returnList;
		} finally {
			pm.close();
		}
	}
	
	public static List<ActionJDO> runActions(PersistenceManager pm, Long runId) {
		Query query = pm.newQuery(ActionJDO.class);
		query.setFilter("runId == runIdParam ");
		query.declareParameters("Long runIdParam");
		return (List<ActionJDO>) query.executeWithArray(runId);
	}

	private static Action toBean(ActionJDO jdo) {
		if (jdo == null)
			return null;
		Action actionBean = new Action();
		actionBean.setAction(jdo.getAction());
		actionBean.setGeneralItemId(jdo.getGeneralItemId());
		actionBean.setGeneralItemType(jdo.getGeneralItemType());
		actionBean.setRunId(jdo.getRunId());
		actionBean.setTime(jdo.getTime());
		actionBean.setTimestamp(jdo.getTime());
		actionBean.setUserEmail(jdo.getUserEmail());
		return actionBean;
	}

	public static void deleteActions(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ActionJDO> visToDelete =runActions(pm, runId);
			pm.deletePersistentAll(visToDelete);
		} finally {
			pm.close();
		}
	}
	
	public static void deleteActions(Long runId, String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ActionJDO> visToDelete =getActionsJDO(pm, runId, null, userId, null, null);
			pm.deletePersistentAll(visToDelete);
		} finally {
			pm.close();
		}
	}
}
