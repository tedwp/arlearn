/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;

import com.google.gwt.json.client.JSONObject;

public abstract class GameNotificationHandler implements NotificationHandler {
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	@Override
	public void onNotification(JSONObject bean) {
		if (bean.containsKey("modificationType")) {
			if (bean.get("modificationType").isNumber().doubleValue() == CREATED) onNewGame(bean.get("game").isObject());
			if (bean.get("modificationType").isNumber().doubleValue() == DELETED) onDeleteGame((long) bean.get("game").isObject().get("gameId").isNumber().doubleValue());
		}
	}
	
	public abstract void onNewGame(JSONObject jsonObject);

	public abstract void onDeleteGame(long gameId);
	
}
