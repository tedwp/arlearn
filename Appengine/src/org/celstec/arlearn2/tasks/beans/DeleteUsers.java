package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.UsersDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteUsers extends GenericBean {

	private Long runId;
	private String userId;
	private String teamId;

	public DeleteUsers() {
		super();
	}

	public DeleteUsers(String token, Long runId, String userId, String teamId) {
		super(token);
		this.runId = runId;
		this.userId = userId;
		this.teamId = teamId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Override
	public void run() {
		try {
			UsersDelegator ud = new UsersDelegator(getToken());
			if (getUserId()!=null) {
				ud.deleteUser(getRunId(), getUserId());
			} 
			if (getTeamId() != null) {
				ud.deleteUser(getTeamId());
			}
			if (getRunId() != null) {
				ud.deleteUser(runId);
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

	}

}
