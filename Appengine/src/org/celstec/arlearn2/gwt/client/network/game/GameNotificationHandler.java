package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;

import com.google.gwt.json.client.JSONObject;

public abstract class GameNotificationHandler implements NotificationHandler {
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	@Override
	public void onNotification(JSONObject bean) {
		System.out.println("notification : "+ bean);
		if (bean.containsKey("modificationType")) {
			if (bean.get("modificationType").isNumber().doubleValue() == CREATED) onNewGame(bean.get("game").isObject());
			if (bean.get("modificationType").isNumber().doubleValue() == DELETED) onDeleteGame((long) bean.get("game").isObject().get("gameId").isNumber().doubleValue());
		}
	}
	
	public abstract void onNewGame(JSONObject jsonObject);

	public abstract void onDeleteGame(long gameId);
	
}