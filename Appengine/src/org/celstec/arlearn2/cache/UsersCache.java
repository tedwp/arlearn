package org.celstec.arlearn2.cache;

import java.util.List;

import org.celstec.arlearn2.beans.run.User;

public class UsersCache  extends RunCache{

	private static UsersCache instance;

	private static String USER_PREFIX = "UsersCache";

	private UsersCache() {
	}

	public static UsersCache getInstance() {
		if (instance == null)
			instance = new UsersCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "UsersCache";
	}
	
	public List<User> getUserList(Long runId, Object... args) {
		String cacheKey = generateCacheKey(USER_PREFIX, runId, args); 
		if (!cacheKeyExists(runId, USER_PREFIX, cacheKey)) return null;
		return (List<User>) getCache().get(cacheKey);
	}
	
	public void putUserList(List<User> pr, Long runId, Object... args) {
		String cacheKey = generateCacheKey(USER_PREFIX, runId, args); 
		storeCacheKey(runId, USER_PREFIX, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeUser(Long runId) {
		removeKeysForGame(runId, USER_PREFIX);
	}

}
