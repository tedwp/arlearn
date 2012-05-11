package org.celstec.arlearn2.delegators;

import java.util.UUID;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.jdo.manager.TeamManager;
import org.celstec.arlearn2.tasks.beans.DeleteUsers;
import org.celstec.arlearn2.cache.TeamsCache;

import com.google.gdata.util.AuthenticationException;

public class TeamsDelegator extends GoogleDelegator {

	private static final Logger logger = Logger.getLogger(TeamsDelegator.class.getName());

	public TeamsDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
	public TeamsDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public Team createTeam(Team team) {
		RunDelegator rd = new RunDelegator(this);
		if (team.getRunId() == null) {
			team.setError("No run identifier specified");
			return team;
		}
		if (rd.getRun(team.getRunId())== null) {
			team.setError("No run with given id exists");
			return team;
		}
		if (team.getTeamId() == null) {
			team.setTeamId(UUID.randomUUID().toString());
		}
		TeamsCache.getInstance().removeTeams(team.getRunId());
		TeamManager.addTeam(team.getRunId(), team.getTeamId(), team.getName());
		return team;
	}

	
	
	public TeamList getTeams(Long runId) {
		TeamList tl = TeamsCache.getInstance().getTeamList(runId);
		if (tl != null)
			return tl;
		tl = TeamManager.getTeams(runId);
		TeamsCache.getInstance().putTeamList(runId, tl);
		return tl;
	}

	public Team getTeam(String teamId) {
		Team returnTeam = TeamsCache.getInstance().getTeam(teamId);
		if (returnTeam == null) {
			returnTeam = TeamManager.getTeam(teamId);
			if (returnTeam == null) {
				returnTeam = new Team();
				returnTeam.setError("no team exists with id " + teamId);
			}
			TeamsCache.getInstance().putTeam(teamId, returnTeam);
		}
		return returnTeam;
	}
	
	public void deleteTeam(Long runId) {
		TeamList tl = getTeams(runId);
		for (Team t : tl.getTeams()) {
			deleteTeam(t.getTeamId());
		}
	}
	
	public void deleteTeam(String teamId) {
		TeamsCache.getInstance().removeTeam(teamId);
		TeamManager.deleteTeam(teamId);
		(new DeleteUsers(getAuthToken(), null, null, teamId)).scheduleTask();
		//TODO test if deleting users works...
	}
}