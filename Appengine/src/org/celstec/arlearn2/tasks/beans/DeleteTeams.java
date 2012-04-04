package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.TeamsDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteTeams extends GenericBean {

	private Long runId;
	private String teamId;

	public DeleteTeams() {
		super();
	}

	public DeleteTeams(String token, Long runId, String teamId) {
		super(token);
		this.runId = runId;
		this.teamId = teamId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
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
			TeamsDelegator td = new TeamsDelegator(getToken());
			if (getRunId() != null) {
				td.deleteTeam(getRunId());
			}
			if (getTeamId() != null) {
				td.deleteTeam(getTeamId());
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

}
