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

import org.celstec.arlearn2.android.asynctasks.network.SynchronizeRunsTask;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkSwitcher extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			return;
		}
		NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		if (networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				Log.d("NW", "Network type: " + networkInfo.getTypeName() + " Network subtype: " + networkInfo.getSubtypeName());
			}
			resyncRuns(context);
		} else {
			Log.e("NW", "Network connection lost");
		}
	}

	public void resyncRuns(Context context) {
//		SynchronizeRunsTask.resetCloudSyncTime();
//		broadCast(context, RunReceiver.action);
		RunDelegator.getInstance().synchronizeRunsWithServer(context);
		GameDelegator.getInstance().synchronizeGamesWithServer(context);
	}
	
//	private void broadCast(Context context, String action) {
//		Intent runIntent = new Intent();
//		runIntent.setAction(action);
//		context.sendBroadcast(runIntent);
//	}

	public static boolean isOnline(Context ctx) {
	    ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
