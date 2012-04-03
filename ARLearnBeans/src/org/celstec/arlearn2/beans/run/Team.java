package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;


public class Team extends RunBean{

	
	private String teamId;
	private String name;
	private List<User> users = new ArrayList<User>();
	
	public Team(){
		
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addUser(User u) {
		this.users.add(u);
	}
	
}
