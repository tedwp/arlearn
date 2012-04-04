package org.celstec.arlearn2.delegators.scoreRecord;

import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ScoreRecord;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserScore;
import org.celstec.arlearn2.cache.ScoreRecordCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.jdo.manager.ScoreRecordManager;

import com.google.gdata.util.AuthenticationException;

public class QueryScoreRecord extends GoogleDelegator {

	public QueryScoreRecord(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public QueryScoreRecord(GoogleDelegator gd) {
		super(gd);
	}

	

	public List<ScoreRecord> getScoreRecordList(Long runId, String action, Long generalItemId, String generalItemType, String scope, String email, String teamId) {
		List<ScoreRecord> result = ScoreRecordManager.getScoreRecord(runId, action, generalItemId, generalItemType, scope, email, teamId);
//		List<ScoreRecord> result = ScoreRecordCache.getInstance().getScoreRecordList(runId, scope, action, generalItemId, generalItemType, email, teamId);
//		if (result == null) {
//			result = ScoreRecordManager.getScoreRecord(runId, action, generalItemId, generalItemType, scope, email, teamId);
//			ScoreRecordCache.getInstance().putScoreRecordList(result, runId, scope, action,generalItemId, generalItemType	, email, teamId);
//		}
		return result;
	}
	
	public ScoreRecord getScoreRecord(Action action, String scope, String teamId) {
		String email = null;
		if ("user".equals(scope)) {
			email = action.getUserEmail();
			teamId = null;
		} else if ("all".equals(scope)) {
			teamId = null;
		}
		List <ScoreRecord> list= getScoreRecordList(action.getRunId(), action.getAction(), action.getGeneralItemId(), action.getGeneralItemType(), scope, email, teamId);
		if (list.isEmpty()) return null;
		return list.get(0);
	}
	
	public UserScore getUserScore(Long runId) {
		UsersDelegator qu = new UsersDelegator(this);
		String email = qu.getCurrentUserAccount();
		User user = qu.getUserByEmail(runId, email);
		if (user == null) {
			UserScore us = new UserScore();
			us.setError("no user exists for given run");
			us.setErrorCode(UserScore.RUNNOTFOUND);
			return us;
		}
		UserScore us = new UserScore();
		us.setUserScore(getUserScore(runId, email));
		us.setTeamScore(getTeamScore(runId, user.getTeamId()));
		us.setTotalScore(us.getUserScore()+us.getTeamScore()+us.getAllScore());
		//TODO check semantics of allscore
		us.setAllScore(getAllScoreForTeam(runId, user.getTeamId()));
		return us;
	}
	
	public Long getUserScore(Long runId, String email) {
		long sum = 0;
		Iterator<ScoreRecord> it = getScoreRecordList(runId, null, null, null, "user", email, null).iterator();
		while (it.hasNext()) {
			sum += ((ScoreRecord) it.next()).getValue();
		}
		return sum;
	}
	
	public Long getTeamScore(Long runId, String teamId) {
		long sum = 0;
		Iterator<ScoreRecord> it = getScoreRecordList(runId, null, null, null, "team", null, teamId).iterator();
		while (it.hasNext()) {
			sum += ((ScoreRecord) it.next()).getValue();
		}
		return sum;
	}
	
	public Long getAllScoreForTeam(Long runId, String teamId) {
		long sum = 0;
		Iterator<ScoreRecord> it = getScoreRecordList(runId, null, null, null, "all", null, teamId).iterator();
		while (it.hasNext()) {
			sum += ((ScoreRecord) it.next()).getValue();
		}
		return sum;
	}

}
