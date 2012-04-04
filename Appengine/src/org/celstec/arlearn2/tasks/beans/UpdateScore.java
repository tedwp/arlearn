package org.celstec.arlearn2.tasks.beans;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.UserScore;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.Notification;
import org.celstec.arlearn2.delegators.notification.ScoreNotification;
import org.celstec.arlearn2.delegators.scoreRecord.QueryScoreRecord;

import com.google.gdata.util.AuthenticationException;

public class UpdateScore extends GenericBean{
	private static final Logger log = Logger.getLogger(UpdateScore.class.getName());

	
	private Long runId;
	private String scope;
	private String userEmail;
	
	public UpdateScore() {
		
	}
	
	public UpdateScore(String token, Long runId, String scope, String userEmail) {
		super(token);
		this.runId = runId;
		this.scope = scope;
		this.userEmail = userEmail;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public void run() {
		int scoreint = Notification.scopeToIntConstant(scope);
		switch (scoreint) {
		case Notification.USER:
			try {
				QueryScoreRecord qsr = new QueryScoreRecord(getToken());
				UserScore us = qsr.getUserScore(getRunId());
				ScoreNotification sn = new ScoreNotification(Notification.USER, getRunId(), getToken());
				sn.notifyScoreUpdate(us, getUserEmail());
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
			break;
		case Notification.TEAM:
			// case team 
			// user + run -> team
			// team -> all users
			// for each user : user -> score ; send score to user
			
			break;
		case Notification.ALL:
			// case all
			// user + run -> all users
			// for each users: user -> score; send score to user
			break;
		default:
			break;
		}
		
		
		
		
//		private void notifyPlayers(String scope, long runId) {
//			ScoreNotification sn = new ScoreNotification(ScoreNotification.TEAM, runId, authToken);
//			sn.notifyPlayers(100);
//			
//		}
		
	}
}
