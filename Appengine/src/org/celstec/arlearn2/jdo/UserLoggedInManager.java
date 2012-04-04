package org.celstec.arlearn2.jdo;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserLoggedInManager {

	public static void submitUser(String email, String authToken) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UsersLoggedIn uli = new UsersLoggedIn();
		uli.setKey(authToken.hashCode());
		uli.setUsername(normalizeEmail(email));
		try {
			pm.makePersistent(uli);
		} finally {
			pm.close();
		}
	}
	
	public static String getUser(String authToken) {
		if (authToken == null || authToken.equals("")) return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (authToken.startsWith("GoogleLogin")) authToken = authToken.substring(authToken.indexOf("auth=")+5);
		Key key = KeyFactory.createKey(UsersLoggedIn.class.getSimpleName(), authToken.hashCode());
		try {
			return ((UsersLoggedIn)pm.getObjectById(UsersLoggedIn.class, key)).getUsername();
		} catch (Exception e) {
			return null;
		}

	}
	
	public static String normalizeEmail(String mail) {
		if (mail == null) return null;
		int posAt = mail.indexOf("@");
		if (posAt != -1) {
			mail = mail.substring(0, posAt);
		}
		return mail.replace(".", "").toLowerCase();
	}

}
