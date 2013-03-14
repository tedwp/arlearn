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
package org.celstec.arlearn2.android.broadcast;

import java.io.Serializable;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BeanReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn.beanbroadcast";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		if (extras != null) {
			Serializable bean = extras.getSerializable("bean");
			try {
				switch (NotificationBeans.valueOf(bean.getClass().getSimpleName())) {
				case RunModification:
					GameDelegator.getInstance().synchronizeParticipateGamesWithServer(context);
					RunDelegator.getInstance().synchronizeRunsWithServer(context);
					break;
				case GeneralItemModification:
					long currentRunId = PropertiesAdapter.getInstance(context).getCurrentRunId();
					GeneralItemModification gim = (GeneralItemModification) bean;
					if (gim.getRunId() == currentRunId) {
						GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(context, gim.getRunId(), gim.getGameId());	
					}
//					reCast("org.celstec.arlearn2.beans.notification.GeneralItemModification", bean, context);
					break;
				case GameModification:
					GameDelegator.getInstance().synchronizeParticipateGamesWithServer(context);
					GameDelegator.getInstance().synchronizeMyGamesWithServer(context);
					break;
				case Ping:
					reCast("org.celstec.arlearn2.beans.notification.Ping", bean, context);
					break;
				default:
					break;
				}
			} catch (IllegalArgumentException e) {
				// eat this
			}
		}
	}

	private void reCast(String action, Serializable bean, Context context) {
		Intent runIntent = new Intent();
		runIntent.setAction(action);
		runIntent.putExtra("bean", bean);
		context.sendBroadcast(runIntent);
	}

	enum NotificationBeans {
		RunModification, GameModification, GeneralItemModification, Ping, Pong, LocationUpdate, Action, Response;
	}

}
