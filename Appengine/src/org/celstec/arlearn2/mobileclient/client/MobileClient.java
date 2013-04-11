package org.celstec.arlearn2.mobileclient.client;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.LoginClient;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.cordova.CordovaEntryPoint;

public class MobileClient extends CordovaEntryPoint {

	public static ArlearnNavigationStack navStack;
	
	
	public MobileClient() {
		navStack = new ArlearnNavigationStack();
	}
	
	@Override
	protected void onDeviceReady() {
//		GenericClient.urlPrefix = "http://streetlearn.appspot.com/rest/";
//		LoginClient.urlPrefix = "http://streetlearn.appspot.com/";
		RootLayoutPanel.get().add(navStack);
	}
	
	
	
}
