package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;

public class NotificationClient extends GenericClient{

	private static NotificationClient instance;
	private static final int ERROR_DESERIALIZING = 1;

	private NotificationClient() {
		super("/notifications");
	}
	
	public static NotificationClient getOauthClient() {
		if (instance == null) {
			instance = new NotificationClient();
		}
		return instance;
	}
	
	public void gcm(String token, GCMDeviceDescription device) {
		executePost(getUrlPrefix()+"/gcm", token, device, null);
	}

}
