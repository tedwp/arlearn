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
package org.celstec.arlearn2.android.asynctasks.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.GeneralItemVisibility.VisibilityEvent;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.VisibleGeneralItemsDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;
import org.json.JSONArray;

import android.media.SoundPool.OnLoadCompleteListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Message;
import android.os.Vibrator;

public class GeneralItemDependencyHandler extends GenericTask implements  DatabaseTask {

//	private Context ctx;
	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static boolean soundPoolLoaded = false;
	private static boolean beep = true;

	public GeneralItemDependencyHandler() {
	}
	
	public void addTaskToQueue(Context ctx) {
		DatabaseHandler dbHandler = DBAdapter.getDatabaseThread(ctx);
		if (!dbHandler.hasMessages(DBAdapter.DEPENDENCIES_MESSAGE)) {
			Message m = Message.obtain(dbHandler);
			m.obj = this;
			m.what = DBAdapter.DEPENDENCIES_MESSAGE;
			dbHandler.sendMessageDelayed(m, 500);
		}
	}
	
	@Override
	public void execute(DBAdapter db) {
		if (soundPool == null) {
			initSoundPool(db.getContext());
		}
		checkDependencies(db);
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());
	}
	
	private synchronized void initSoundPool(Context ctx) {
		if (soundPool == null) {
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			soundPoolMap = new HashMap<Integer, Integer>();
			soundPoolMap.put(1, soundPool.load(ctx, R.raw.multi_new, 1));
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					soundPoolLoaded = true;
				}
			});
		}
	}

	public void checkDependencies(DBAdapter db) {
		checkDependencies(db, (new PropertiesAdapter(db.getContext())).getCurrentRunId());
	}

	public void checkDependencies(DBAdapter db, long runId) {
		beep = (new PropertiesAdapter(db.getContext())).getCurrentRunId() == runId;
		List<Action> actions = ActionCache.getInstance().getActions(runId);
		if (actions != null) {
			processItemsNotYetInitialised(db, runId);
			processItemsNotYetVisible(db, runId, actions);
			processItemsNotYetDisappeared(db, runId, actions);
		}
	}

	public void processItemsNotYetInitialised(DBAdapter db, final long runId) {
		VisibilityEvent[] events = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NOT_INITIALISED);
//		TreeSet<GeneralItem> items = GeneralItemVisibilityCache.getInstance().getAllNotInitializedItems(runId);
		for (VisibilityEvent e: events) {
			GeneralItem gi = GeneralItemsDelegator.getInstance().getGeneralItem(e.itemId);
			if (itemMatchesPlayersRole(db, runId, gi)) {
				db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, 0, GeneralItemVisibility.NOT_YET_VISIBLE);
			}
		}
//		if (items != null)
//			for (GeneralItem gi : items) {
//				if (itemMatchesPlayersRole(db, runId, gi)) {
//					Dependency dep = gi.getDependsOn();
//					DBAdapter.getAdapter(db.getContext()).getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, 0, GeneralItemVisibility.NOT_YET_VISIBLE);
//				}
//			}
	}

	private static boolean containsRole(JSONArray userRoles, String role) {
		try {
			for (int i = 0; i < userRoles.length(); i++) {

				if (userRoles.get(i).equals(role))
					return true;
			}
		} catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean itemMatchesPlayersRole(DBAdapter db, long runId, GeneralItem gi) {
		if (gi.getRoles() != null && !gi.getRoles().isEmpty()) {
			String userRoles = db.getRunAdapter().queryRoles(runId);
			if (userRoles != null && !"".equals(userRoles)) {
				try {
					boolean playerHasRequiredRole = true;
					JSONArray userRolesJson = new JSONArray(userRoles);
					for (String giRole : gi.getRoles()) {
						playerHasRequiredRole &= containsRole(userRolesJson, giRole);
					}
					return playerHasRequiredRole;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} 
		return true;
	}

	public void processItemsNotYetVisible(final DBAdapter db, final long runId, final List<Action> actions) {
		VisibilityEvent[] events = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NOT_YET_VISIBLE);
		for (VisibilityEvent e: events) {
			GeneralItem generalItem = GeneralItemsDelegator.getInstance().getGeneralItem(e.itemId);
			if (generalItem != null) {
				Dependency dep = generalItem.getDependsOn();
				long satisfiedAt = -1; //item not yet visible
				if (dep != null) {
					satisfiedAt = dep.satisfiedAt(actions); 
				} else {
					satisfiedAt = 0; //item supposed to visible from time 0
				}
				if (satisfiedAt != -1) { //if update is necessary
					db.getGeneralItemVisibility().setVisibilityStatus(generalItem.getId(), runId, satisfiedAt, GeneralItemVisibility.VISIBLE);
				}
			}
			
		}
//		Long[] itemIds = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NOT_YET_VISIBLE);
//		for (int i = 0; i < itemIds.length; i++) {
//			if (!DBAdapter.getDatabaseThread(db.getContext()).hasMessages((int) itemIds[i].longValue())) { //TODO make this safer
//				final GeneralItem generalItem = GeneralItemsCache.getInstance().getGeneralItems(itemIds[i]); 
//				if (generalItem == null)
//					return;
//				Dependency dep = generalItem.getDependsOn();
//				long satisfiedAtTemp = -1;
//				if (dep != null) {
//					satisfiedAtTemp = dep.satisfiedAt(actions);
//				} else {
//					satisfiedAtTemp = 0;
//				}
//				final long satisfiedAt = satisfiedAtTemp;
//				final long itemId = generalItem.getId();
//				if (satisfiedAt != -1) {
//					generalItem.setVisibleAt(satisfiedAt);
//					VisibleGeneralItemsDelegator.getInstance().checkRoleAndMakeItemVisible(db, runId, generalItem);
//				}
//			}
//		}
	}

	public void processItemsNotYetDisappeared(final DBAdapter db, final long runId, final List<Action> actions) {
		VisibilityEvent[] visibleEvents = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.VISIBLE);
		VisibilityEvent[] noLongerVisibleEvents = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NO_LONGER_VISIBLE);

//		Long[] itemIds = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.VISIBLE);
//		Long[] nolongVis = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NO_LONGER_VISIBLE);
		ArrayList<VisibilityEvent> visible = new ArrayList<VisibilityEvent>();
		for (VisibilityEvent visEvent: visibleEvents) {
			visible.add(visEvent);
		}
		for (VisibilityEvent novisLong: noLongerVisibleEvents) {
			visible.remove(novisLong);
		}
		for (VisibilityEvent e: visible) {
//			if (!DBAdapter.getDatabaseThread(db.getContext()).hasMessages((int) itemId.longValue())) { //TODO make this safer
				final GeneralItem generalItem = GeneralItemsCache.getInstance().getGeneralItems(e.itemId); 
				if (generalItem == null)
					return;
				Dependency dep = generalItem.getDisappearOn();
				long satisfiedAtTemp = -1;
				if (dep != null) {
					satisfiedAtTemp = dep.satisfiedAt(actions);
					final long satisfiedAt = satisfiedAtTemp;
					if (satisfiedAt != -1) {
						db.getGeneralItemVisibility().setVisibilityStatus(e.itemId, runId, satisfiedAt, GeneralItemVisibility.NO_LONGER_VISIBLE);
//						ForceUpdateTask.scheduleEvent(db.getContext(), runId, false, null);
					}
				}
//			}
		}
	}
	
	public static void broadcastTroughIntent(GeneralItem gi, Context ctx, long runId) {
		if (new PropertiesAdapter(ctx).getCurrentRunId() != runId) return;
		if (gi.getAutoLaunch() != null && gi.getAutoLaunch()) {
			if (gi != null) {
				ActivityUpdater.closeActivities(ctx, gi.getId(), NarratorItemActivity.class.getCanonicalName());
				GIActivitySelector.startActivity(ctx, gi, true);
			}
				
		} else {
			Intent updateIntent = new Intent();
			updateIntent.setAction("org.celstec.arlearn.updateActivities");
			updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
			updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
			updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
			ctx.sendBroadcast(updateIntent);
		}

		if (gi != null)
			vibrateRingPhone(ctx);

	}

	public static long lastVibration = System.currentTimeMillis();

	public static void vibrateRingPhone(Context ctx) {
		if (!beep)
			return;
		long now = System.currentTimeMillis();
		if (now - lastVibration < 1500)
			return;
		lastVibration = now;
		playSound(1, ctx);
		Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200, 200, 500, 200, 200 }, -1);

	}

	public static void playSound(final int sound, Context ctx) {
		AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		final float volume = streamVolumeCurrent / streamVolumeMax;
		/* Play the sound with the correct volume */

		Thread waitThread = new Thread() {
			public void run() {

				int counter = 0;
				try {
					while (!soundPoolLoaded && counter < 20) {
						sleep(200);
						counter++;

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			}
		};
		waitThread.run();
	}

	@Override
	protected void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		
	}

	
}
