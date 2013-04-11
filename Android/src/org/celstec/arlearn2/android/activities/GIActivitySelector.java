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
package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.ScanTag;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceImageActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.genItemActivities.OpenBadgeActivity;
import org.celstec.arlearn2.android.genItemActivities.ScanTagActivity;
import org.celstec.arlearn2.android.genItemActivities.SingleChoiceActivity;
import org.celstec.arlearn2.android.genItemActivities.SingleChoiceImageActivity;
import org.celstec.arlearn2.android.genItemActivities.VideoObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.YoutubeObjectActivity;

import android.content.Context;
import android.content.Intent;

public class GIActivitySelector {

	
	public static void startActivity(Context ctx, GeneralItem gi) {
		startActivity(ctx, gi, false);
	}
	
	public static void startActivity(Context ctx, GeneralItem gi, boolean newTask) {
		Long runId = PropertiesAdapter.getInstance(ctx).getCurrentRunId();
		Long gameId = RunCache.getInstance().getGameId(runId);
		if (runId != null && gameId != null) {
			Intent i = null;
			i = new Intent(new Intent(ctx, getCorrespondingActivity(gi)));
			if (newTask) i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(GeneralItem.class.getCanonicalName(), gi);
			i.putExtra(Run.class.getCanonicalName(), runId);
			i.putExtra(Game.class.getCanonicalName(), gameId);
			ctx.startActivity(i);
		}
	}
	
	public static Class getCorrespondingActivity(GeneralItem gi) {
		if (gi.getType().equals(MultipleChoiceTest.class.getName())) {
			 return MultipleChoiceActivity.class;
		}
		if (gi.getType().equals(SingleChoiceTest.class.getName())) {
			 return SingleChoiceActivity.class;
		}
		if (gi.getType().equals(SingleChoiceImageTest.class.getName())) {
			 return SingleChoiceImageActivity.class;
		}
		if (gi.getType().equals(MultipleChoiceImageTest.class.getName())) {
			 return MultipleChoiceImageActivity.class;
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
		if (gi.getType().equals(ScanTag.class.getName())) {
			return  ScanTagActivity.class;
		}
		return  NarratorItemActivity.class;
	}
}
