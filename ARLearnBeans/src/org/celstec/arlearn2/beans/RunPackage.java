package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;

public class RunPackage extends Bean{
	private Run run;
	private List<Team> teams = new ArrayList<Team>();
	
	public RunPackage() {
		
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}
	
	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}

}
