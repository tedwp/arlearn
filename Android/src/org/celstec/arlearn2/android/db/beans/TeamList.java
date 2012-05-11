package org.celstec.arlearn2.android.db.beans;

import java.util.ArrayList;
import java.util.List;

public class TeamList {
	public static String teamType = "org.celstec.arlearn2.android.db.beans.Team";

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

