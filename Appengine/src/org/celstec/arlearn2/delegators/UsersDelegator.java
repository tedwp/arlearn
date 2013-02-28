/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.delegators;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.cache.UserLoggedInCache;
import org.celstec.arlearn2.cache.UsersCache;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.delegators.notification.NotificationEngine;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.DeleteActions;
import org.celstec.arlearn2.tasks.beans.DeleteBlobs;
import org.celstec.arlearn2.tasks.beans.DeleteResponses;
import org.celstec.arlearn2.tasks.beans.DeleteScoreRecords;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItemsVisibility;

import com.google.gdata.util.AuthenticationException;

public class UsersDelegator extends GoogleDelegator {

	public UsersDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public UsersDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public User createUser(User u) {
		User check = checkUser(u);
		if (check != null) return check;
		u.setEmail(User.normalizeEmail(u.getEmail()));
		UserManager.addUser(u);
		UsersCache.getInstance().removeUser(u.getRunId()); //removing because user might be cached in a team
//		(new NotifyUpdateRun(authToken,u.getRunId(), true, false, u.getEmail())).scheduleTask();
		
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.CREATED);
		rm.setRun((new RunDelegator(this)).getRun(u.getRunId()));
		NotificationEngine.getInstance().notify(u.getEmail(), rm);
//		ChannelNotificator.getInstance().notify(u.getEmail(), rm);
		
		(new UpdateGeneralItemsVisibility(authToken, u.getRunId(), u.getEmail(), 1)).scheduleTask();
		
		return u;
	}
	
	private User checkUser(User u) {
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
		return null;
	}
	
	public User selfRegister(User u, Run run) {
		User check = checkUser(u);
		if (check != null) return check;
		UsersCache.getInstance().removeUser(u.getRunId()); //removing because user might be cached in a team
		u.setEmail(User.normalizeEmail(u.getEmail()));
		
		UserManager.deleteUser(u.getRunId(), u.getEmail());
		
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED);
		rm.setRun(run);
		ChannelNotificator.getInstance().notify(u.getEmail(), rm);
		
		UserManager.addUser(u);
		
		rm = new RunModification();
		rm.setModificationType(RunModification.CREATED);
		rm.setRun(run);
		ChannelNotificator.getInstance().notify(u.getEmail(), rm);
		
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
		email = User.normalizeEmail(email);
		List<User> users = getUserList(runId, null, email, null);
		if (users.isEmpty()) return null;
		return users.get(0);		
	}
	
	public HashMap<String, User> getUserMap(Long runId) {
		HashMap<String, User> map = new HashMap<String, User>();
		for (User u: getUserList(runId, null, null, null)){
			map.put(u.getEmail(), u);
		}
		return map;
	}
	
	public User deleteUser(Long runId, String email) {
		email = User.normalizeEmail(email);
		User user = getUserByEmail(runId, email);
//		UserManager.deleteUser(runId, email);
		UserManager.setStatusDeleted(runId, email);
		UsersCache.getInstance().removeUser(runId); //removing because user might be cached in a team
		//(new NotifyUpdateRun(authToken,runId, false, true, user.getEmail())).scheduleTask();
		(new DeleteActions(authToken, runId, email)).scheduleTask();
		(new DeleteBlobs(authToken, runId, email)).scheduleTask();
		(new DeleteResponses(authToken, runId, email)).scheduleTask();
		(new DeleteScoreRecords(authToken, runId, email)).scheduleTask();
		(new UpdateGeneralItemsVisibility(authToken, runId, email, 2)).scheduleTask();
		notifyRunDeleted(runId, email);
		
		return user;
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
//			UserManager.deleteUser(runId, u.getEmail());
			UserManager.setStatusDeleted(runId, u.getEmail());

			notifyRunDeleted(runId, u.getEmail());
		}
		if (runId != null) UsersCache.getInstance().removeUser(runId);
	}
	
	private void notifyRunDeleted(long runId, String email) {
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED	);
		rm.setRun(new Run());
		rm.getRun().setRunId(runId);
		NotificationEngine.getInstance().notify(email, rm);
//		ChannelNotificator.getInstance().notify(email, rm);
	}
	
}
