package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;

public class TeamList extends RunBean{
	
	public static String teamsType = "org.celstec.arlearn2.beans.run.Team";
	
	private List<Team> teams = new ArrayList<Team>();

	public TeamList() {

	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> team) {
		this.teams = team;
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}

}
