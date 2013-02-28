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
