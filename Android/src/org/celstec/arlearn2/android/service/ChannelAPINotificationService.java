package org.celstec.arlearn2.android.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.broadcast.GeneralItemReceiver;
import org.celstec.arlearn2.android.broadcast.RunReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.gvsu.cis.masl.channelAPI.ChannelAPI;
import edu.gvsu.cis.masl.channelAPI.ChannelService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ChannelAPINotificationService extends Service {

	public final static int ONLINE_STATUS = 1;
	public final static int OFFLINE_STATUS = 2;
	public final static int RESTARTING = 3;
	private ChannelThread thread;
	private long lastTrafficTimeStamp;
	
	private ChannelService chatListener;
	private ChannelAPI channel;
	private long lastCreate;
	private PropertiesAdapter pa;
	 CountDownLatch startLatch;
	 
	private void broadcast() {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
		sendBroadcast(updateIntent);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startLatch = new CountDownLatch(1);
		if (pa == null) pa = new PropertiesAdapter(this);
		if (thread == null) {
			thread = new ChannelThread(this);
			thread.start();
		} else {
			startLatch.countDown();
		}
		
		
//		else {
//			
//		}
		
		try {
			startLatch.await();
			Message restart = Message.obtain(thread.channelHandler, 1);
			restart.sendToTarget();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
	
	

	class ChannelThread extends Thread {
		
		
		private Context ctx;
		protected ChannelHandler channelHandler;
		
		
		public ChannelThread(Context ctx) {
			this.ctx = ctx;
		}
		
//		private void createChatListener() {
//			chatListener = new ChannelService() {
//
//				
//
//			};
//		}

		
//		private void checkStopService() {
//			if ((System.currentTimeMillis() - lastCreate) < 120000) {
//				startChannelListener();
//			} else {
//				Log.i("stopSelf", "");
//				ChannelAPINotificationService.this.stopSelf();
//				ChannelAPINotificationService.this.thread = null;
//			}
//		}
		
		
		
		
		@Override
		public void run() {
//			lastCreate = System.currentTimeMillis();
//			if (chatListener == null) {
//				createChatListener();
//			}

//			if (pa.getStatus() == OFFLINE_STATUS)
//				startChannelListener();
			try {
				Looper.prepare();
				channelHandler = new ChannelHandler();
				startLatch.countDown();
				Looper.loop();
				
			} catch (Throwable t) {
				Log.e("channel", "channel thread halted", t);
			}
		}
	}
	
	public class ChannelHandler extends Handler {
		public final static int RESTART = 1;
		public final static int ONOPEN = 2;
		public final static int ONTRAFFIC = 3;
		public final static int ONERROR = 4;
		public final static int ONCLOSE = 5;
		public final static int ONMESSAGE = 6;

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case RESTART:
				System.out.println("restart "+message+" "+message.what);
				restartChannel();
				break;
			case ONOPEN:
				System.out.println("ONOPEN "+message+" "+message.what);
				onOpen();
				break;
			case ONTRAFFIC:
				System.out.println("ONTRAFFIC "+message+" "+message.what);
				onTraffic();
				break;
			case ONERROR:
				System.out.println("ONERROR "+message+" "+message.what);
				onError();
				break;
			case ONCLOSE:
				System.out.println("ONCLOSE "+message+" "+message.what);
				onClose();
				break;
			case ONMESSAGE:
				System.out.println("ONMESSAGE "+message+" "+message.what);
				onMessage((String)message.obj);
				break;

			default:
				break;
			}
		}
	}
	 
	protected void restartChannel() {
		int status = pa.getStatus();
		if (!(status == RESTARTING) && (checkChannelTimeOut() || status == OFFLINE_STATUS)) {
			pa.setStatus(RESTARTING);
			if (channel != null) {
				try {
					channel.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				channel = null;
			}
			startChannelListener();
		
			//we were offline...
			//TODO resync
			System.out.println("---STARTING RESYNC---");
			resync();
		}
	}
	
	public boolean checkChannelTimeOut(){
		System.out.println("checkChannelTimeOut "+(lastTrafficTimeStamp< System.currentTimeMillis()-60000));
		return (lastTrafficTimeStamp< System.currentTimeMillis()-60000);
	}
	
	private void stopChannelService() {
		pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
		broadcast();
//		thread = null;
	}
	
	private void startChannelListener() {
		pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
		broadcast();
		String token = pa.getFusionAuthToken();
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String url = GenericClient.urlPrefix;
			if (!url.endsWith("/"))
				url += "/";
			HttpGet request = new HttpGet(url + "rest/channelAPI/token");
//			HttpGet request = new HttpGet(url + "rest/channelAPI/token3m");
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			HttpResponse resp = httpClient.execute(request);
			if (resp.getEntity() == null)
				return;
			String entry = EntityUtils.toString(resp.getEntity());
			JSONObject object = new JSONObject(entry);
			entry = object.getString("token");
			channel = new ChannelAPI(GenericClient.urlPrefix, pa.getUsername(), thread.channelHandler);
			channel.setChannelId(entry);
			channel.open();
		} catch (Exception e) {
			stopChannelService();
			e.printStackTrace();
		}

	}
	
	public void onOpen() {
		System.out.println("on Open");
		pa.setStatus(ChannelAPINotificationService.ONLINE_STATUS);
		broadcast();

	}

	public void onMessage(String message) {
		try {
			Serializable messageSerializeable = (Serializable) JsonBeanDeserializer.deserialize(message);
			Intent intent = new Intent();
			intent.setAction("org.celstec.arlearn.beanbroadcast");
			intent.putExtra("bean", messageSerializeable);
			ChannelAPINotificationService.this.sendBroadcast(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void onClose() {
		stopChannelService();
	}

	public void onError() {
		stopChannelService();
	}

	
	public void onTraffic() {	
		lastTrafficTimeStamp = System.currentTimeMillis();
		pa.setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
		broadcast();
		pa.setStatus(ChannelAPINotificationService.ONLINE_STATUS);

		broadcast();


		System.out.println("on traffic "+lastTrafficTimeStamp);
	}
	
	public void resync() {
		Intent runSyncIntent = new Intent();
		runSyncIntent.setAction(RunReceiver.action);
		sendBroadcast(runSyncIntent);
		
		Intent gimIntent = new Intent();
		gimIntent.setAction(GeneralItemReceiver.action);
		sendBroadcast(gimIntent);
		
	}

}
