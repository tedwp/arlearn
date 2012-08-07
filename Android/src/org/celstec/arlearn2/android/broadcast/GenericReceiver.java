package org.celstec.arlearn2.android.broadcast;


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
	

}
