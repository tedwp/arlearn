package org.celstec.arlearn2.delegators;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.jdo.manager.IOSDevicesRegistryManager;

import com.google.gdata.util.AuthenticationException;

public class ApplePushNotificationDelegator extends GoogleDelegator {

	public ApplePushNotificationDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public ApplePushNotificationDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public ApplePushNotificationDelegator() {
		super();
	}

	public void registerDescription(APNDeviceDescription descriptionDevice) {
		if (descriptionDevice.getAccount() == null || descriptionDevice.getDeviceToken() == null || descriptionDevice.getDeviceUniqueIdentifier() == null)
			return;
		IOSDevicesRegistryManager.addDevice(descriptionDevice);
	}

	public void sendNotification(String account, String text) {
		for (String token: IOSDevicesRegistryManager.getDeviceTokens(account)){
			sendNotification(account, token, text);
		}

	}
	public void sendNotificationAsJson(String account, String text) {
		for (String token: IOSDevicesRegistryManager.getDeviceTokens(account)){
			sendNotificationAsJson(account, token, text);
		}

	}
	
	public void sendNotification(String account, String deviceToken, String text) {
		String json = "{\"aps\":{\"alert\":\""+text+"\", \"requestType\":1, \"sound\":\"default\"}}";
		sendNotificationAsJson(account, deviceToken, json);
	}
	
	public void sendNotificationAsJson(String account, String deviceToken, String json) {
		
        try {
            URL url = new URL("http://sharetec.celstec.org/APN/not.php?deviceToken="+deviceToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json);
            writer.close();
    
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
            } else {
                // Server returned HTTP error code.
            }
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
	}
}
