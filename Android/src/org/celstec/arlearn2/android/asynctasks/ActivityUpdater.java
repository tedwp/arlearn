package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.genItemActivities.VideoObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.YoutubeObjectActivity;


import android.content.Context;
import android.content.Intent;

public class ActivityUpdater {

	public static void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
			if (activities[i].equals(NarratorItemActivity.class.getCanonicalName())) {
				updateIntent.putExtra(AudioObjectActivity.class.getCanonicalName(), true); 
				updateIntent.putExtra(VideoObjectActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(YoutubeObjectActivity.class.getCanonicalName(), true);
			}
		}
		ctx.sendBroadcast(updateIntent);
	}
	
}
