package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.genItemActivities.OpenBadgeActivity;
import org.celstec.arlearn2.android.genItemActivities.ScanTagActivity;
import org.celstec.arlearn2.android.genItemActivities.VideoObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.YoutubeObjectActivity;


import android.content.Context;
import android.content.Intent;

public class ActivityUpdater {

	public final static String ITEM_NO_TO_CLOSE = "ITEM_NO_TO_CLOSE";
	
	public static void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
			if (activities[i].equals(NarratorItemActivity.class.getCanonicalName())) {
				updateIntent.putExtra(AudioObjectActivity.class.getCanonicalName(), true); 
				updateIntent.putExtra(VideoObjectActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(YoutubeObjectActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(ScanTagActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(OpenBadgeActivity.class.getCanonicalName(), true);
			}
		}
		ctx.sendBroadcast(updateIntent);
	}
	
	public static void closeActivities(Context ctx, long itemId, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ITEM_NO_TO_CLOSE, itemId);

		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
			if (activities[i].equals(NarratorItemActivity.class.getCanonicalName())) {
				updateIntent.putExtra(AudioObjectActivity.class.getCanonicalName(), true); 
				updateIntent.putExtra(VideoObjectActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(YoutubeObjectActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(ScanTagActivity.class.getCanonicalName(), true);
				updateIntent.putExtra(OpenBadgeActivity.class.getCanonicalName(), true);
			}
		}
		ctx.sendBroadcast(updateIntent);
	}
	
	
}
