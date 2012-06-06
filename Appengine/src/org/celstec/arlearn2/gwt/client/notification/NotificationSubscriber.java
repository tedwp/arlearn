package org.celstec.arlearn2.gwt.client.notification;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class NotificationSubscriber {

	private static NotificationSubscriber instance;

	private HashMap<String, NotificationHandler> notificationMap = new HashMap<String, NotificationHandler>();
	
	private ChannelCreatedCallback channelCreatedCallback = new ChannelCreatedCallback() {
		@Override
		public void onChannelCreated(Channel channel) {
			channel.open(new SocketListener() {
				@Override
				public void onOpen() {
					Window.alert("Channel opened!");
				}

				@Override
				public void onMessage(String message) {
					dispatchMessage(message);
				}

				@Override
				public void onError(SocketError error) {
					instance = null;
//					Window.alert("Error: " + error.getDescription());
				}

				@Override
				public void onClose() {
					instance = null;
//					Window.alert("Channel closed!");
				}
			});
		}
	};
	private NotificationSubscriber() {
		requestToken();
	}

	public static NotificationSubscriber getInstance() {
		if (instance == null)
			instance = new NotificationSubscriber();
		return instance;
	}

	public void openChannel(String token) {
		ChannelFactory.createChannel(token, channelCreatedCallback);
	}

	public void requestToken() {
		ChannelClient.getInstance().getToken(new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				openChannel(jsonValue.isObject().get("token").isString().stringValue());
			}
		});
	}
	
	private void dispatchMessage(String message) {
		JSONValue jsonValue = JSONParser.parseLenient(message);
		JSONObject object = jsonValue.isObject();
		if ( object != null && object.containsKey("type")) {
			NotificationHandler handler = notificationMap.get(object.get("type").isString().stringValue());
			if (handler != null) handler.onNotification(object);
		}
	}
	
	public void addNotificationHandler(String type, NotificationHandler handler) {
		notificationMap.put(type, handler);
	}
	
	public void removeAllHandlers() {
		notificationMap = new HashMap<String, NotificationHandler>();
	}
}