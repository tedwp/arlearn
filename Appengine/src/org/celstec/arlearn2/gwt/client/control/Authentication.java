package org.celstec.arlearn2.gwt.client.control;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.network.LoginCallback;
import org.celstec.arlearn2.gwt.client.network.LoginClient;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import com.google.gwt.user.client.Cookies;

public class Authentication implements LoginCallback {

	public static final String USERNAME_COOKIE = "org.celstec.arlearn.const.username";
	public static final String AUTH_COOKIE = "org.celstec.arlearn.const.auth";
	static Authentication instance;
	
	private Authentication() {
		
	}
	
	public static Authentication getInstance() {
		if (instance == null) instance = new Authentication();
		return instance;
	}
	
	public void userCredentialsReceived(String username, String password) {
		LoginClient lc = new LoginClient(username, password);
		Cookies.setCookie(USERNAME_COOKIE, username);	
		lc.authenticate(this);
	}

	@Override
	public void onAuthenticationTokenReceived(String string) {
		if (string.startsWith("\"")) string = string.substring(1);
		if (string.endsWith("\"")) string = string.substring(0, string.length()-1);
		
		Cookies.setCookie(AUTH_COOKIE, string);	

		Authoring.enableTabs();
		
	}

	@Override
	public void onError() {
		Authoring.loginIncorrect();
		Cookies.removeCookie(AUTH_COOKIE);		
		Cookies.removeCookie(USERNAME_COOKIE);		
	}
	
	public void disAuthenticate() {
		Cookies.removeCookie(AUTH_COOKIE);		
		Cookies.removeCookie(USERNAME_COOKIE);
		Authoring.disableTabs();
		Authoring.clearCaches();
		NotificationSubscriber.getInstance().removeAllHandlers();
	}
	
	public boolean isAuthenticated() {
		return Cookies.getCookie(AUTH_COOKIE) != null;
	}
	
	public String getAuthenticationToken() {
		return Cookies.getCookie(AUTH_COOKIE);
	}
	
	public String getCurrentUser() {
		return Cookies.getCookie(USERNAME_COOKIE);
	}
}
