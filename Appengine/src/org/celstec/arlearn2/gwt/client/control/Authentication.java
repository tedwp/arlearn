package org.celstec.arlearn2.gwt.client.control;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.network.LoginCallback;
import org.celstec.arlearn2.gwt.client.network.LoginClient;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.user.client.Cookies;

public class Authentication implements LoginCallback {

	static Authentication instance;
	
	private Authentication() {
		
	}
	
	public static Authentication getInstance() {
		if (instance == null) instance = new Authentication();
		return instance;
	}
	
	public void userCredentialsReceived(String username, String password) {
		LoginClient lc = new LoginClient(username, password);
		Cookies.setCookie("username", username);	
		lc.authenticate(this);
	}

	@Override
	public void onAuthenticationTokenReceived(String string) {
		if (string.startsWith("\"")) string = string.substring(1);
		if (string.endsWith("\"")) string = string.substring(0, string.length()-1);
		
		Cookies.setCookie("auth", string);	

		Authoring.enableTabs();
		
	}

	@Override
	public void onError() {
		System.out.println("login did not succeed");
		Authoring.loginIncorrect();
		Cookies.removeCookie("auth");		
		Cookies.removeCookie("username");		
	}
	
	public void disAuthenticate() {
		Cookies.removeCookie("auth");		
		Cookies.removeCookie("username");
		Authoring.disableTabs();
		NotificationSubscriber.getInstance().removeAllHandlers();
	}
	
	public boolean isAuthenticated() {
		return Cookies.getCookie("auth") != null;
	}
	
	public String getAuthenticationToken() {
		return Cookies.getCookie("auth");
	}
}
