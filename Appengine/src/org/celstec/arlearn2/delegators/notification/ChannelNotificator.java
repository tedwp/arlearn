package org.celstec.arlearn2.delegators.notification;

import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChannelNotificator {

	private static ChannelNotificator instance;
	
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	private ChannelNotificator() {
	}
	
	public static ChannelNotificator getInstance() {
		if (instance == null) instance = new ChannelNotificator();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		notify(account, bean.toString());
	}
	
	public void notify(String account, String message) {
		channelService.sendMessage(new ChannelMessage(account, message));
	}
}
