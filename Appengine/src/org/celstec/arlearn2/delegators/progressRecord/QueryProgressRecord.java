package org.celstec.arlearn2.delegators.progressRecord;

import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.ProgressRecord;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.manager.ProgressRecordManager;
import org.celstec.arlearn2.cache.ProgressRecordCache;

import com.google.gdata.util.AuthenticationException;

public class QueryProgressRecord extends GoogleDelegator {

	public QueryProgressRecord(GoogleDelegator gd) {
		super(gd);
	}

	public QueryProgressRecord(String authToken) throws AuthenticationException {
		super(authToken);
	}
	
	public ProgressRecord getProgressRecord(Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
			if ("user".equals(scope)) {
				teamId = null;
			} else if ("team".equals(scope)) {
				email = null;
			} else if ("all".equals(scope)) {
				teamId = null;
				email = null;
			}
			Iterator<ProgressRecord> it = getProgressRecordList(runId, action, generalItemId, generalItemType, scope, email, teamId).iterator();
			if (it.hasNext()) {
				ProgressRecord result = it.next(); 
				return result;
			}
			return null;
	}
	
	public List<ProgressRecord> getProgressRecordList(Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		List<ProgressRecord> result = ProgressRecordCache.getInstance().getProgressRecordList(runId, scope, action, generalItemId, generalItemType, email, teamId);
		if (result == null) {
			result = ProgressRecordManager.getProgressRecord(runId, action, generalItemId, generalItemType, scope, email, teamId);
			ProgressRecordCache.getInstance().putProgressRecordList(result, runId, scope, action,generalItemId, generalItemType	, email, teamId);
		}
		return result;
		
	}
	
	public Long getUserProgress(Long runId, String email) {
		return new Long(getProgressRecordList(runId, null, null, null, "user", email, null).size());
	}
	
	public Long getTeamProgress(Long runId, String teamId) {
		return new Long(getProgressRecordList(runId, null, null, null, "team", null, teamId).size());
	}
	
	public Long getAllProgressForTeam(Long runId, String teamId) {
		return new Long(getProgressRecordList(runId, null, null, null, "all", null, teamId).size());
	}

}
