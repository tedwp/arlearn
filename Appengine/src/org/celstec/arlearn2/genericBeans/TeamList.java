package org.celstec.arlearn2.genericBeans;

import java.util.ArrayList;
import java.util.List;

public class TeamList {

	private List<Team> team = new ArrayList<Team>();

	public TeamList() {
	}

	public void addTeam(Team team) {
		this.team.add(team);
	}

	public List<Team> getTeam() {
		return team;
	}

	public void setTeam(List<Team> team) {
		this.team = team;
	}
	

}

