package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.broadcast.MediaService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class GenericBroadcastReceiver {

	private Activity activity;
	public static final String ACTION = "org.celstec.arlearn.updateActivities";

	public GenericBroadcastReceiver(Activity context) {
		this.activity = context;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Boolean forMe = intent.getExtras().getBoolean(activity.getClass().getCanonicalName(), false);
			if (forMe) {
				if (((ARLearnBroadcastReceiver) activity).showStatusLed())
					LedStatus.updateStatus(activity);
				((ARLearnBroadcastReceiver) activity).onBroadcastMessage(intent.getExtras());
			}
		}
	};

	public void onPause() {
		if (broadcastReceiver != null)
			activity.unregisterReceiver(broadcastReceiver);
	}

	public void onResume() {
		if (broadcastReceiver != null)
			activity.registerReceiver(broadcastReceiver, new IntentFilter(ACTION));
		if (((ARLearnBroadcastReceiver) activity).showStatusLed())
			LedStatus.updateStatus(activity);
		Intent intent = new Intent(activity, MediaService.class);
		activity.startService(intent);
	}

}
