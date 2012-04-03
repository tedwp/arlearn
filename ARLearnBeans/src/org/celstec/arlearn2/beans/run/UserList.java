package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;


public class UserList extends RunBean{
	
	public static String usersType = "org.celstec.arlearn2.beans.run.User";
	
	private List<User> users = new ArrayList<User>();

	public UserList() {

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
