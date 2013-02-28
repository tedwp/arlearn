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


import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class GenericReceiver extends BroadcastReceiver{

	
	protected void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);	
		}
		ctx.sendBroadcast(updateIntent);
	}
	

	//TODO stop channelapi
	public static void setStatusToLogout(Context context) {
		PropertiesAdapter.getInstance(context).disAuthenticate();
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
		context.sendBroadcast(updateIntent);
	}
}
