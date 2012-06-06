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
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Run;
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

	ChannelService chatListener;
	
	ChannelAPI channel;
	
	private long lastCreate;
		
	private void createChatListener() {
		chatListener = new ChannelService(){

			public void onOpen() {
				System.out.println("on Open");
				
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
				checkStopService();
				
			}

			public void onError(Integer errorCode, String description) {
				System.out.println("error: shutting down service");
				checkStopService();
				
			}
			
		};
	}

	private void checkStopService(){
		Log.i("lastCreate", "" + lastCreate+ " "+System.currentTimeMillis());
		Log.i("System.currentTimeMillis()", "" + (System.currentTimeMillis() - lastCreate));
		if ((System.currentTimeMillis() - lastCreate) < 120000) {
			startChannelListener();
		} else {
			Log.i("stopSelf", "");

			stopSelf();
		}
	}
		
	private void startChannelListener() {
		Log.i("recreate", "i will start");
		String token = PropertiesAdapter.getInstance(this).getFusionAuthToken();
//		String token = "DQAAALsAAACV-2Fzguc4EtSbFfCkLZPkhwTRLgob-18T5URC9SazewtYAzLBqo3VaDoWn9l0vstbZ_MQQY3tdf2E3Di-1UhApXwh79TzQOBHuutcoXfBxhu3yCU0Jrpb6AA6yF7u7ccB2C5W2X3h7VZYNK46hIvCQGHoNTyBEWS8Xd0vM_ROqEd2CU1GOsF53qrp7KU-JffJP6wkMLh-z8I2Z6LCKa8oIbZ9aoEuZvmD63bbAGZv14jcMGFbwUwSo3Dhwi1h8UM";
		HttpClient httpClient = new DefaultHttpClient();
		try {
//			HttpGet request = new HttpGet("http://streetlearn.appspot.com/rest/channelAPI/token2m");
			HttpGet request = new HttpGet("http://10.0.2.2:7777/rest/channelAPI/token");
			request.setHeader("Authorization", "GoogleLogin auth=" + token);

			HttpResponse resp = httpClient.execute(request);
			String entry = EntityUtils.toString(resp.getEntity());
			JSONObject object = new JSONObject(entry);
			entry = object.getString("token");
//			entry = entry.substring(entry.indexOf("token") + 8);
			Log.i("entry", "is:" + entry);
//			channel = new ChannelAPI("http://streetlearn.appspot.com", "arlearn1", chatListener);
			channel = new ChannelAPI("http://10.0.2.2:7777", "arlearn1", chatListener);
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
		if (chatListener != null) return START_NOT_STICKY; 
		createChatListener();		
		startChannelListener();
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

