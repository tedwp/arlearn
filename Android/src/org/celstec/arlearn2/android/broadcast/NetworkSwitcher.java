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

}