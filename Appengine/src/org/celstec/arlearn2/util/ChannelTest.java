package org.celstec.arlearn2.util;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ChannelTest {

	
	public static String getUserId() {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		String token= channelService.createChannel("someid");
		return token;
	}
	
	public static void sendUpdate(String channelKey) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		channelService.sendMessage(new ChannelMessage(channelKey, "hallo"));
	}
}
