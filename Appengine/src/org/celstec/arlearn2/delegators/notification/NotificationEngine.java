package org.celstec.arlearn2.delegators.notification;

public class NotificationEngine {

	private static NotificationEngine instance;
	
	
	private NotificationEngine() {
	}
	
	public static NotificationEngine getInstance() {
		if (instance == null) instance = new NotificationEngine();
		return instance;
	}
	
	public void notify(String account, Object bean) {
		for (NotificationChannel c: getNotificationChannels(account)) {
			c.notify(account, bean);
		}
	}
	
	private NotificationChannel[] getNotificationChannels(String account) {
		NotificationChannel[] result;
		int amount  = 1;
		if (account.equals("arlearn1")) {
			amount++;
		}
		result = new NotificationChannel[amount];
		result[0] = ChannelNotificator.getInstance();
		if (account.equals("arlearn1")) {
			result[1] = APNNotificationChannel.getInstance();

		}
		return result;
	}

}
