/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
