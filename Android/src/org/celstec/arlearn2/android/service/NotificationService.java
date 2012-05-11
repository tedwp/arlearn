package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends  IntentService {

	public NotificationService() {
		super("NotificationService");
	}
	private PropertiesAdapter pa;
	Handler mHandler;
	XMPPConnection connection;
    public static final String BROADCAST_ACTION = "org.celstec.arlearn2.xmpp";

	private void setUpConnection() {
		ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
		connection = new XMPPConnection(connConfig);
		try {
			connection.connect();
			String username = pa.getUsername();
			if (!username.contains("@")) username += "@gmail.com";
			connection.login(username, pa.getPassword());
			connection.getRoster().createEntry("streetlearn@appspot.com", "streetlearn", null);
			if (connection != null) {
				NotificationListener nl = new NotificationListener(NotificationService.this);
				PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
				connection.addPacketListener(nl, filter);
				connection.addConnectionListener(new ConnectionListener() {
					
					
					public void reconnectionSuccessful() {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "reconnectionSuccessful", Toast.LENGTH_SHORT).show();
					        }
					    });
						
					}
					
					
					public void reconnectionFailed(Exception arg0) {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "is not connected", Toast.LENGTH_SHORT).show();
					        }
					    });						
					}
					
					
					public void reconnectingIn(int arg0) {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "reconnectingIn", Toast.LENGTH_SHORT).show();
					        }
					    });						
					}
					
					
					public void connectionClosedOnError(Exception arg0) {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "Error: internet connection closed", Toast.LENGTH_SHORT).show();
					        }
					    });						
					}
					
					
					public void connectionClosed() {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "connectionClosed", Toast.LENGTH_SHORT).show();
					        }
					    });						
					}
				});
				Presence presence = new Presence(Presence.Type.available);
				connection.sendPacket(presence);			
				}
			
		} catch (XMPPException e) {
			Log.e("xmpp", e.getMessage(), e);
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();
		intent = new Intent(BROADCAST_ACTION);	
		pa = new PropertiesAdapter(this);
		setUpConnection();
		
	}


	public void processXmppNotification(NotificationBean bean) {
		bean.run(this);
		if (bean.requiresBroadcast()) {
			intent.putExtra("bean", bean);
	    	sendBroadcast(intent);
		}

	}
	Intent intent;

	@Override
	public void onDestroy() {
		super.onDestroy();
		connection.disconnect();
	}
	private boolean continueRunning = true;

	protected void onHandleIntent(Intent intent) {
		int time = 0;

		while (continueRunning) {
			// Log.i("service", "is still running");
			synchronized (this) {
				try {
					if (!connection.isConnected()) {
						mHandler.post(new Runnable() {            
					        
					        public void run() {
								Toast.makeText(NotificationService.this, "no internet connection", Toast.LENGTH_SHORT).show();
					        }
					    });
						setUpConnection();
					}
					
					time += 1;
					wait(4000);
					
				} catch (InterruptedException e) {
					Log.e("interrupt", e.getMessage(), e);
				}
			}
		}
	}

}
