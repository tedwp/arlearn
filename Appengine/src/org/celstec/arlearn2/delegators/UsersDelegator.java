package org.celstec.arlearn2.delegators;

import java.util.List;

import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.cache.UserLoggedInCache;
import org.celstec.arlearn2.cache.UsersCache;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.NotifyUpdateRun;

import com.google.gdata.util.AuthenticationException;

public class UsersDelegator extends GoogleDelegator {

	public UsersDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public UsersDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public User createUser(User u) {
		if (u.getRunId() == null) {
			u.setError("No run identifier specified");
			return u;
		}
		TeamList tl = (new TeamsDelegator(this)).getTeams(u.getRunId());
		if (!tl.getTeams().isEmpty()) {
			Team dbTeam = null;
			for (Team t : tl.getTeams()) {
				if (t.getTeamId().equals(u.getTeamId()))
					dbTeam = t;
			}
			if (dbTeam == null) {
				u.setError("teamId does not exist in db");
				return u;
			}
		}
		u.setEmail(UserLoggedInManager.normalizeEmail(u.getEmail()));
		UserManager.addUser(u.getRunId(), u.getTeamId(), u.getEmail(), u.getName());
		UsersCache.getInstance().removeUser(u.getRunId()); //removing because user might be cached in a team
		(new NotifyUpdateRun(authToken,u.getRunId(), true, false, u.getEmail())).scheduleTask();
		return u;
	}

	
	
	public String getCurrentUserAccount () {
		String accountName = UserLoggedInCache.getInstance().getUser(this.authToken);
		if (accountName == null) {
			accountName = UserLoggedInManager.getUser(this.authToken);
			if (accountName != null) UserLoggedInCache.getInstance().putUser(this.authToken, accountName);
		}
		return accountName;
	}
	
	public List<User> getUserList(Long runId, String name, String email, String teamId) {
		List<User> users = UsersCache.getInstance().getUserList(runId, name, email, teamId);
		if (users == null) {
			users = UserManager.getUserList(name, email, teamId, runId);
			UsersCache.getInstance().putUserList(users, runId, name, email, teamId);
		}
		return users;
	}
	
	/**
	 * retrieves all users of the given run.
	 * 
	 * @param runId
	 * @return
	 */
	public UserList getUsers(Long runId) {
		return getUsers(runId, null);
	}

	/**
	 * retrieves all users of the given run and teamId.
	 * 
	 * @param runId
	 * @param teamId when teamId is null, all users within the run are returned
	 * @return
	 */
	public UserList getUsers(Long runId, String teamId) {
		List<User> users = getUserList(runId, null, null, teamId);
		UserList returnList = new UserList();
		returnList.setRunId(runId);
		returnList.setUsers(users);
		return returnList;
	}
	
	/**
	 * retrieves a user for a run, given the email identifier of the user.
	 * 
	 * @param runId
	 * @param email
	 * @return
	 */
	public User getUserByEmail(Long runId, String email) {
		email = UserLoggedInManager.normalizeEmail(email);
		List<User> users = getUserList(runId, null, email, null);
		if (users.isEmpty()) return null;
		return users.get(0);		
	}
	
	public void deleteUser(Long runId, String email) {
		email = UserLoggedInManager.normalizeEmail(email);
		User user = getUserByEmail(runId, email);
		UserManager.deleteUser(runId, email);
		UsersCache.getInstance().removeUser(runId); //removing because user might be cached in a team
	}

	public void deleteUser(long runId) {
		List<User> userList = getUserList(runId, null, null, null);
		for (User u : userList) {
			deleteUser(runId, u.getEmail());
		}
	}
	
	public void deleteUser(String teamId) {
		List<User> userList = getUserList(null, null, null, teamId);
		Long runId = null;
		for (User u : userList) {
			runId = u.getRunId();
			UserManager.deleteUser(runId, u.getEmail());
			(new NotifyUpdateRun(authToken,runId, false, true, u.getEmail())).scheduleTask();
		}
		if (runId != null) UsersCache.getInstance().removeUser(runId);
	}
	
}
