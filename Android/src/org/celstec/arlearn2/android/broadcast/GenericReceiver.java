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
