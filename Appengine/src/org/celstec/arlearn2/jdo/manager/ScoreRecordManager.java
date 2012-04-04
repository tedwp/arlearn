package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.ScoreRecord;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ActionJDO;
import org.celstec.arlearn2.jdo.classes.ScoreRecordJDO;

public class ScoreRecordManager {

	private static final String params[] = new String[]{"runId", "action", "generalItemId", "generalItemType", "scope", "userEmail","teamId"};
	private static final String paramsNames[] = new String[]{"runIdParam", "actionParam", "generalItemIdParam", "generalItemTypeParam", "scopeParam", "userEmailParam","teamIdParam"};
	private static final String types[] = new String[]{"Long", "String", "Long", "String", "String", "String", "String"};

	public static ScoreRecord addScoreRecord(String action, Long generalItemId, String generalItemType, Long runId, String scope, String teamId, String userEmail, long value) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ScoreRecordJDO scoreRecord = new ScoreRecordJDO();
		scoreRecord.setAction(action);
		scoreRecord.setGeneralItemId(generalItemId);
		scoreRecord.setGeneralItemType(generalItemType);
		scoreRecord.setRunId(runId);
		scoreRecord.setScope(scope);
		scoreRecord.setTeamId(teamId);
		scoreRecord.setUserEmail(userEmail);
		scoreRecord.setValue(value);
		try {
			return toBean(pm.makePersistent(scoreRecord));
		} finally {
			pm.close();
		}
	}
	
	public static List<ScoreRecord> getScoreRecord(Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		ArrayList<ScoreRecord> returnScoreRecord = new ArrayList<ScoreRecord>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
//		Query query = pm.newQuery(ScoreRecordJDO.class);
//		Object args [] = new Object[]{runId, action, generalItemId, generalItemType, scope, email, teamId};
//		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
//		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
//		Iterator<ScoreRecordJDO> it = ((List<ScoreRecordJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args))).iterator();
		Iterator<ScoreRecordJDO> it = (getScoreRecord(pm, runId, action, generalItemId, generalItemType, scope, email, teamId)).iterator();
		while (it.hasNext()) {
			returnScoreRecord.add(toBean((ScoreRecordJDO) it.next()));
		}
		return returnScoreRecord;
	}
	
	public static List<ScoreRecordJDO> getScoreRecord(PersistenceManager pm, Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		Query query = pm.newQuery(ScoreRecordJDO.class);
		Object args [] = new Object[]{runId, action, generalItemId, generalItemType, scope, email, teamId};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return  ((List<ScoreRecordJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}

	
	private static ScoreRecord toBean(ScoreRecordJDO jdo) {
		if (jdo == null) return null;
		ScoreRecord scoreRecord = new ScoreRecord();
		scoreRecord.setRunId(jdo.getRunId());
		scoreRecord.setAction(jdo.getAction());
		scoreRecord.setGeneralItemId(jdo.getGeneralItemId());
		scoreRecord.setGeneralItemType(jdo.getGeneralItemType());
		scoreRecord.setScope(jdo.getScope());
		scoreRecord.setTeamId(jdo.getTeamId());
		scoreRecord.setTimestamp(jdo.getTime());
		scoreRecord.setEmail(jdo.getUserEmail());
		scoreRecord.setValue(jdo.getValue());
		
		return scoreRecord;
	}

	public static void deleteScoreRecords(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ScoreRecordJDO> toDelete =getScoreRecord(pm, runId, null, null, null, null, null, null);
			pm.deletePersistentAll(toDelete);
		} finally {
			pm.close();
		}
	}
	
}
