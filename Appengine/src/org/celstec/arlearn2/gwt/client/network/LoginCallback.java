package org.celstec.arlearn2.gwt.client.network;

public interface LoginCallback {
	
	public void onAuthenticationTokenReceived(String string);
	public void onError();

}
