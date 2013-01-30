package org.celstec.arlearn2.delegators.notification;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.celstec.arlearn2.beans.notification.NotificationBean;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.ApplePushNotificationDelegator;
import org.celstec.arlearn2.jdo.manager.IOSDevicesRegistryManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class APNNotificationChannel implements NotificationChannel{
	private static APNNotificationChannel instance;
	
	
	private APNNotificationChannel() {
	}
	
	public static APNNotificationChannel getInstance() {
		if (instance == null) instance = new APNNotificationChannel();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		for (String token: IOSDevicesRegistryManager.getDeviceTokens(account)){
			try {
				sendNotification(account, token, ((NotificationBean)bean));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendNotification(String account, String deviceToken, NotificationBean bean) throws JSONException {
		String text = "";
		bean.retainOnlyIdentifier();
//		if (bean instanceof RunModification) {
//			RunModification rm = (RunModification) bean;
//			Run r = rm.getRun();
//			rm.setRun(new Run());
//			rm.getRun().setRunId(r.getRunId());
//			JSONObject json = new JSONObject(rm.toString());
//			JSONObject aps = new JSONObject();
//			aps.put("aps", json);
//			json.put("alert", "new run");
//			json.put("sound", "default");
////			json.remove("run");	
//			
//		}
		
		JSONObject json = new JSONObject(bean.toString());
		JSONObject aps = new JSONObject();
		aps.put("aps", json);
		json.put("alert", "notification");
		if (bean instanceof RunModification) {
			RunModification rm = (RunModification) bean;
			if (rm.getModificationType() == RunModification.CREATED) {
				json.put("alert", "new run available");		
			}
			if (rm.getModificationType() == RunModification.DELETED) {
				json.put("alert", "Run deleted");		
			}
		}
		json.put("sound", "default");
		text = aps.toString();
		System.out.println("tesxt "+text);
		
        try {
            URL url = new URL("http://sharetec.celstec.org/APN/not.php?deviceToken="+deviceToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(text);
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
