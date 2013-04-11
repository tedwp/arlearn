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

import org.celstec.arlearn2.android.broadcast.MediaService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class GenericBroadcastReceiver {

	private Activity activity;
	public static final String ACTION = "org.celstec.arlearn.updateActivities";
	public static final String RENDER = "render";

	public GenericBroadcastReceiver(Activity context) {
		this.activity = context;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras() != null) {
				Boolean forMe = intent.getExtras().getBoolean(activity.getClass().getCanonicalName(), false);
				Boolean render = intent.getExtras().getBoolean(RENDER, true);
				if (forMe) {
					if (((ARLearnBroadcastReceiver) activity).showStatusLed())
						LedStatus.updateStatus(activity);
					((ARLearnBroadcastReceiver) activity).onBroadcastMessage(intent.getExtras(), render);
				}
			}
		}
	};

	public void onPause() {
		try {
			if (broadcastReceiver != null)
				activity.unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
