package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.ResponseJDO;

public class ResponseManager {

	public static final String params[] = new String[]{"runId",  "generalItemId", "userEmail", "timeStamp", "revoked"};
	private static final String paramsNames[] = new String[]{"runIdParam", "generalItemIdParam", "userEmailParam", "timeStampParam", "revokedParam"};
	private static final String types[] = new String[]{"Long", "Long", "String", "String", "Long", "Boolean"};

	
	public static void addResponse(Long generalItemId, String responseValue, Long runId, String userEmail, Long timeStamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ResponseJDO responseRecord = new ResponseJDO();
		responseRecord.setGeneralItemId(generalItemId);
		responseRecord.setResponseValue(responseValue);
		responseRecord.setRunId(runId);
		responseRecord.setUserEmail(userEmail);
		responseRecord.setTimeStamp(timeStamp);
		responseRecord.setRevoked(false);
		try {
			pm.makePersistent(responseRecord);
		} finally {
			pm.close();
		}
	}
	
	public static Response revokeResponse(Long runId, Long generalItemId, String userEmail, Long timestamp){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ResponseJDO> respList = getResponse(pm, runId, generalItemId, userEmail, timestamp, false);
			if (respList.isEmpty()) {
				Response r = new Response();
				r.setError("could not retrieve response from database, hence revoking is not possible");
				return r;
			}
			Iterator<ResponseJDO> it = respList.iterator();
			ResponseJDO rjdo = null;
			if (it.hasNext()) {
				rjdo = ((ResponseJDO) it.next());
			}
			rjdo.setRevoked(true);
			return toBean(rjdo);
		} finally {
			pm.close();
		}
	}
	
	public static List<Response> getResponse(Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {
		ArrayList<Response> returnProgressDefinitions = new ArrayList<Response>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<ResponseJDO> it = getResponse(pm, runId, generalItemId, userEmail, timestamp, revoked).iterator();
			while (it.hasNext()) {
				returnProgressDefinitions.add(toBean((ResponseJDO) it.next()));
			}
			return returnProgressDefinitions; 
		} finally {
			pm.close();
		}
	}
	
	private static List<ResponseJDO> getResponse(PersistenceManager pm, Long runId, Long generalItemId, String userEmail, Long timestamp, Boolean revoked) {
		Query query = pm.newQuery(ResponseJDO.class);
		Object args [] = new Object[]{runId, generalItemId, userEmail, timestamp, revoked};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<ResponseJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}
	
	private static Response toBean(ResponseJDO jdo) {
		if (jdo == null) return null;
		Response pd = new Response();
		pd.setRunId(jdo.getRunId());
		pd.setGeneralItemId(jdo.getGeneralItemId());
		pd.setTimestamp(jdo.getTimeStamp());
		pd.setUserEmail(jdo.getUserEmail());
		pd.setResponseValue(jdo.getResponseValue());
		return pd;
	}

	public static void deleteResponses(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<ResponseJDO>  list = getResponse(pm, runId, null, null, null, null);
			pm.deletePersistentAll(list);
		} finally {
			pm.close();
		}
	}
}
