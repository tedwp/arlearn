package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import net.sf.jsr107cache.Cache;

public class UsersCache {

	
	private static UsersCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(UsersCache.class
			.getName());

	private UsersCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static UsersCache getInstance() {
		if (instance == null)
			instance = new UsersCache();
		return instance;
	}
	
	private static String USERS_PREFIX = "Users";

	public int getUsersTableIdForRun(String authToken, String runId) {
		Integer returnInt = (Integer) cache.get(USERS_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putUsersTableIdForRun(String authToken, String runId, Integer tableId) {
		cache.put(USERS_PREFIX+authToken+runId, tableId);
	}
	
	
	public User getUser(Long runId, String email) {
		return (User) cache.get(USERS_PREFIX+runId+User.normalizeEmail(email));
	}
	
	public void putUser(Long runId, User u) {
		if (u.getEmail() != null && !u.getEmail().equals("")) {
			cache.put(USERS_PREFIX+runId+User.normalizeEmail(u.getEmail()), u);
		}
	}

	public void removeUser(Long runId, User u) {
		if (u.getEmail() != null && !u.getEmail().equals("")) {
			cache.remove(USERS_PREFIX+runId+User.normalizeEmail(u.getEmail()));
		}
	}

	
}
