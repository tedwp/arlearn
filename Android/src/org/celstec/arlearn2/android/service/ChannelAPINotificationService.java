package org.celstec.arlearn2.android.service;
import java.io.IOException;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.gvsu.cis.masl.channelAPI.ChannelAPI;
import edu.gvsu.cis.masl.channelAPI.ChannelAPI.ChannelException;
import edu.gvsu.cis.masl.channelAPI.ChannelService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ChannelAPINotificationService extends Service {

	public final static int ONLINE_STATUS = 1;
	public final static int OFFLINE_STATUS = 2;
	ChannelService chatListener;
	
	ChannelAPI channel;
	
	private long lastCreate;
	
	PropertiesAdapter pa;
		
	private void broadcast(){
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
	}
	
	private void createChatListener() {
		chatListener = new ChannelService(){

			public void onOpen() {
				System.out.println("on Open");
				pa.setStatus(ChannelAPINotificationService.ONLINE_STATUS);
				broadcast();
				
			}

			public void onMessage(String message) {
				System.out.println("message");
				System.out.println("message "+message);
				try {
					Serializable messageSerializeable = (Serializable) JsonBeanDeserializer.deserialize(message);
					Intent intent = new Intent();
					intent.setAction("org.celstec.arlearn.beanbroadcast");
					intent.putExtra("bean", messageSerializeable);
					ChannelAPINotificationService.this.sendBroadcast(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}

			public void onClose() {
				System.out.println("close");
				pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
				broadcast();
				checkStopService();
				
			}

			public void onError(Integer errorCode, String description) {
				System.out.println("error: shutting down service");
				pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
				broadcast();
				checkStopService();
				
			}
			
		};
	}

	private void checkStopService(){
		if ((System.currentTimeMillis() - lastCreate) < 120000) {
			startChannelListener();
		} else {
			Log.i("stopSelf", "");

			stopSelf();
		}
	}
		
	private void startChannelListener() {
		pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
		String token = PropertiesAdapter.getInstance(this).getFusionAuthToken();
//		String token = "DQAAALsAAACV-2Fzguc4EtSbFfCkLZPkhwTRLgob-18T5URC9SazewtYAzLBqo3VaDoWn9l0vstbZ_MQQY3tdf2E3Di-1UhApXwh79TzQOBHuutcoXfBxhu3yCU0Jrpb6AA6yF7u7ccB2C5W2X3h7VZYNK46hIvCQGHoNTyBEWS8Xd0vM_ROqEd2CU1GOsF53qrp7KU-JffJP6wkMLh-z8I2Z6LCKa8oIbZ9aoEuZvmD63bbAGZv14jcMGFbwUwSo3Dhwi1h8UM";
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String url = GenericClient.urlPrefix;
			if (!url.endsWith("/")) url += "/";
			HttpGet request = new HttpGet(url+"rest/channelAPI/token");
			System.out.println("url "+url+"rest/channelAPI/token");
//			HttpGet request = new HttpGet("http://10.0.2.2:7777/rest/channelAPI/token");
			request.setHeader("Authorization", "GoogleLogin auth=" + token);

			HttpResponse resp = httpClient.execute(request);
			if (resp.getEntity() == null) return;
			String entry = EntityUtils.toString(resp.getEntity());
			JSONObject object = new JSONObject(entry);
			entry = object.getString("token");
//			entry = entry.substring(entry.indexOf("token") + 8);
			channel = new ChannelAPI(GenericClient.urlPrefix, pa.getUsername(), chatListener);
//			channel = new ChannelAPI("http://streetlearn.appspot.com", "arlearn1", chatListener);
//			channel = new ChannelAPI("http://10.0.2.2:7777", "arlearn1", chatListener);
			channel.setChannelId(entry);
			channel.open();
		} catch (ParseException e) {
			System.err.print("exception in executeGET");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ChannelException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lastCreate = System.currentTimeMillis();
		if (chatListener == null) {
			createChatListener(); 
		}
		if (pa == null) {
			pa = new PropertiesAdapter(this);
			startChannelListener();
		}
		if (pa.getStatus() == OFFLINE_STATUS) startChannelListener();
		return START_NOT_STICKY;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public static void startService(Context ctx) {
		 Intent i = new Intent(ctx, ChannelAPINotificationService.class);
	     ctx.startService(i);
	}

}

