package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.run.Team;

public class TeamClient  extends GenericClient{

	private static TeamClient instance;

	private TeamClient() {
		super("/team");
	}
	
	public static TeamClient getTeamClient() {
		if (instance == null) {
			instance = new TeamClient();
		}
		return instance;
	}
	
	public Team createTeam(String token, Team team) {
		return (Team) executePost(getUrlPrefix(), token, team, Team.class);
	}
}
