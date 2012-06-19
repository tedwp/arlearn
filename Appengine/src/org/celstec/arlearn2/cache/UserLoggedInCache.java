package org.celstec.arlearn2.cache;

import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

import org.celstec.arlearn2.util.FusionCache;

import com.google.appengine.api.utils.SystemProperty;

public class UserLoggedInCache extends GenericCache{

	private static UserLoggedInCache instance;

	private static String PROGRESSDEFINITION_PREFIX = SystemProperty.applicationVersion.get()+"ProgressDefinition";

	private UserLoggedInCache() {
	}

	public static UserLoggedInCache getInstance() {
		if (instance == null)
			instance = new UserLoggedInCache();
		return instance;

	}
	
	private static String PREFIX = "UsersLoggedInCache";
	
	public String getUser(String authToken) {
		return (String) getCache().get(PREFIX+authToken);
	}
	
	public void putUser(String authToken, String account) {
		getCache().put(PREFIX+authToken, account);
	}
}
