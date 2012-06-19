package org.celstec.arlearn2.cache;

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;

import com.google.appengine.api.utils.SystemProperty;

public class TeamsCache extends GenericCache {

	private static TeamsCache instance;
	
	private TeamsCache() {
	
	}

	public static TeamsCache getInstance() {
		if (instance == null)
			instance = new TeamsCache();
		return instance;
	}
	

	private static String TEAMS_PREFIX_T = SystemProperty.applicationVersion.get()+"teamst";
	private static String TEAMS_PREFIX_R = SystemProperty.applicationVersion.get()+"teamsr";
	
	public Team getTeam(String teamId) {
		return (Team) getCache().get(TEAMS_PREFIX_T+teamId);
	}
	
	public void putTeam(String teamId, Team team) {
		 getCache().put(TEAMS_PREFIX_T+teamId, team);
	}
	
	public void removeTeam(String teamId) {
		 getCache().remove(TEAMS_PREFIX_T+teamId);
	}
	
	public TeamList getTeamList(Long runId) {
		return (TeamList)  getCache().get(TEAMS_PREFIX_R+runId);
	}
	
	public void putTeamList(Long runId, TeamList teamList) {
		 getCache().put(TEAMS_PREFIX_R+runId, teamList);
	}
	
	public void removeTeams(Long runId) {
		 getCache().remove(TEAMS_PREFIX_R+runId);
	}
	
}

