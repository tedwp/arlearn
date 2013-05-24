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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescriptionList;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.jdo.manager.GCMDevicesRegistryManager;
import org.celstec.arlearn2.jdo.manager.IOSDevicesRegistryManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.apphosting.api.search.AclPb.Entry;
import com.google.gdata.util.AuthenticationException;

public class NotificationDelegator extends GoogleDelegator {

	public NotificationDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public NotificationDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public NotificationDelegator() {
		super();
	}

	public void registerDescription(APNDeviceDescription descriptionDevice) {
		if (descriptionDevice.getAccount() == null || descriptionDevice.getDeviceToken() == null || descriptionDevice.getDeviceUniqueIdentifier() == null)
			return;
		IOSDevicesRegistryManager.addDevice(descriptionDevice);
	}

	public void registerDescription(GCMDeviceDescription descriptionDevice) {
		if (descriptionDevice.getAccount() == null || descriptionDevice.getRegistrationId() == null)
			return;
		GCMDevicesRegistryManager.addDevice(descriptionDevice);
	}

	public DeviceDescriptionList listDevices(String account) {
		List<DeviceDescription> iOSDevices = IOSDevicesRegistryManager.getDeviceTokens(account);
		List<DeviceDescription> gcmDevices = GCMDevicesRegistryManager.getDeviceTokens(account);
		DeviceDescriptionList returnList = new DeviceDescriptionList();
		returnList.addDevices(iOSDevices);
		returnList.addDevices(gcmDevices);
		return returnList;
	}

	public void sendNotification(String account, String text) {
		for (DeviceDescription deviceDesc : IOSDevicesRegistryManager.getDeviceTokens(account)) {
			sendNotification(account, ((APNDeviceDescription) deviceDesc).getDeviceToken(), text);
		}

	}

	public void sendiOSNotificationAsJson(String account, String text) {
		for (DeviceDescription deviceDesc : IOSDevicesRegistryManager.getDeviceTokens(account)) {
			sendiOSNotificationAsJson(account, ((APNDeviceDescription) deviceDesc).getDeviceToken(), text);
		}
	}

	public void sendiOSNotificationAsJson(String account, HashMap<String, Object> valueMap) {
		for (DeviceDescription deviceDesc : IOSDevicesRegistryManager.getDeviceTokens(account)) {
			sendiOSNotificationAsJson(account, ((APNDeviceDescription) deviceDesc).getDeviceToken(), valueMap);
		}
	}

	private void sendGCMNotification(String account, String notification) {
		for (DeviceDescription deviceDesc : GCMDevicesRegistryManager.getDeviceTokens(account)) {
			GCMDeviceDescription gcmDesc = (GCMDeviceDescription) deviceDesc;
			sendGCMNotificationAsJson(account, gcmDesc.getRegistrationId(), notification);
		}
	}

	private void sendGCMNotification(String account, HashMap<String, Object> valueMap) {
		for (DeviceDescription deviceDesc : GCMDevicesRegistryManager.getDeviceTokens(account)) {
			GCMDeviceDescription gcmDesc = (GCMDeviceDescription) deviceDesc;
			sendGCMNotificationAsJson(account, gcmDesc.getRegistrationId(), valueMap);
		}

	}

	public void sendNotification(String account, String deviceToken, String text) {
		String json = "{\"aps\":{\"alert\":\"" + text + "\", \"requestType\":1, \"sound\":\"default\"}}";
		sendiOSNotificationAsJson(account, deviceToken, json);
	}

	public void sendGCMNotificationAsJson(String account, String registrationId, HashMap<String, Object> valueMap) {
		Sender sender = new Sender("AIzaSyBBHzixGmnJnu8YhZS44zCObl85JTspo_Q");
		Message.Builder builder = new Message.Builder();
		for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
			builder.addData(entry.getKey(), "" + entry.getValue());
		}
		Message message = builder.build();
		try {
			Result result = sender.send(message, registrationId, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGCMNotificationAsJson(String account, String registrationId, String json) {
		Sender sender = new Sender("AIzaSyBBHzixGmnJnu8YhZS44zCObl85JTspo_Q");
		Message message = new Message.Builder().addData("runId", "1234").build();

		try {
			Result result = sender.send(message, registrationId, 5);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendiOSNotificationAsJson(String account, String deviceToken, String json) {

		try {
			URL url = new URL("http://sharetec.celstec.org/APN/not.php?deviceToken=" + deviceToken);
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

	public void sendiOSNotificationAsJson(String account, String deviceToken, HashMap<String, Object> valueMap) {

		try {
			URL url = new URL("http://sharetec.celstec.org/APN/not.php?deviceToken=" + deviceToken);
			System.out.println("url "+url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			JSONObject aps = new JSONObject();
			JSONObject payload = new JSONObject();
			try {
				aps.put("aps", payload);

				if (!valueMap.containsKey("alert")) {
					payload.put("alert", "notification");
				}

				for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
					payload.put(entry.getKey(), "" + entry.getValue());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println("sending "+aps.toString());
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(aps.toString());
			writer.close();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("ok");
			} else {
				System.out.println("error code"+connection.getResponseCode());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String notification, String account) {
		try {
			JSONObject json = new JSONObject(notification);
			Iterator<String> it = json.keys();
			HashMap<String, Object> valueMap = new HashMap<String, Object>();
			while (it.hasNext()) {
				String key = it.next();
				if (!(json.get(key) instanceof JSONObject))
					valueMap.put(key, json.get(key));

			}
			System.out.println("valueMap " + valueMap);

			sendGCMNotification(account, valueMap);
			// sendGCMNotification(account, json.toString());

			 sendiOSNotificationAsJson(account, valueMap); //TODO rename
			// this one
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}