package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;
import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.genItemActivities.OpenBadgeActivity;
import org.celstec.arlearn2.android.genItemActivities.VideoObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.YoutubeObjectActivity;

import android.content.Context;
import android.content.Intent;

public class GIActivitySelector {

	
	public static void startActivity(Context ctx, GeneralItem gi) {
		startActivity(ctx, gi, false);
	}
	
	public static void startActivity(Context ctx, GeneralItem gi, boolean newTask) {
		Intent i = null;
			i = new Intent(new Intent(ctx, getCorrespondingActivity(gi)));
			i.putExtra(Constants.ITEM_ID, gi.getId()); 
			if (newTask) i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("generalItem", gi);
			ctx.startActivity(i);
//		}
	}
	
	public static Class getCorrespondingActivity(GeneralItem gi) {
		if (gi.getType().equals(MultipleChoiceTest.class.getName())) {
			 return MultipleChoiceActivity.class;
		}
		if (gi.getType().equals(AudioObject.class.getName())) {
			return  AudioObjectActivity.class;
		}
		if (gi.getType().equals(VideoObject.class.getName())) {
			return  VideoObjectActivity.class;
		}
		if (gi.getType().equals(YoutubeObject.class.getName())) {
			return  YoutubeObjectActivity.class;
		}
		if (gi.getType().equals(OpenBadge.class.getName())) {
			return  OpenBadgeActivity.class;
		}
		return  NarratorItemActivity.class;
	}
}
