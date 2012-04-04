package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import net.sf.jsr107cache.Cache;

public class TeamsCache {

	private static TeamsCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(TeamsCache.class
			.getName());

	private TeamsCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static TeamsCache getInstance() {
		if (instance == null)
			instance = new TeamsCache();
		return instance;
	}
	
	private static String MYTEAMS_PREFIX = "myTeams";

	public int getTeamTableIdForRun(String authToken, String runId) {
		Integer returnInt = (Integer) cache.get(MYTEAMS_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putTeamTableIdForRun(String authToken, String runId, Integer tableId) {
		cache.put(MYTEAMS_PREFIX+authToken+runId, tableId);
	}
	
	private static String TEAMS_PREFIX = "teams";

	
	public Team getTeam(Long runId, String id) {
		return (Team) cache.get(TEAMS_PREFIX+runId+id);
	}
	
	public void putTeam(Long runId, Team t) {
		if (t.getTeamId() != null && !t.getTeamId().equals("")) {
			cache.put(TEAMS_PREFIX+runId+t.getTeamId(), t);
		}
		
	}
	
}
