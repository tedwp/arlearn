package org.celstec.arlearn2.android.service;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONArray;

import android.media.SoundPool.OnLoadCompleteListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class GeneralItemDependencyHandler {

	private Context ctx;
	private HashMap<String, Runnable> threads = new HashMap<String, Runnable>();
	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static boolean soundPoolLoaded = false;
	private boolean beep = true;	

	@SuppressLint("NewApi")
	public GeneralItemDependencyHandler(Context ctx) {
		this.ctx = ctx;
		if (soundPool == null) {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(ctx, R.raw.multi_new, 1));
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPoolLoaded = true;
				System.out.println("sound pool is loaded "+soundPoolLoaded);

			}
		});
		}
	}

	public synchronized void checkDependencies() {
		checkDependencies((new PropertiesAdapter(ctx)).getCurrentRunId());
	}
	public synchronized void checkDependencies(long runId) {
		beep = (new PropertiesAdapter(ctx)).getCurrentRunId() == runId;
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		List<Action> actions = ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).query(runId);
		db.close();
		db = new DBAdapter(ctx);
		db.openForWrite();
		processItemsNotYetInitialised(db, runId);
		db.close();
		db = new DBAdapter(ctx);
		db.openForWrite();
		processItemsNotYetVisible(db, runId, actions);
		db.close();
	}

	public void processItemsNotYetInitialised(DBAdapter db, long runId) {
		GeneralItemAdapter giAdap = ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER));

		GeneralItem[] giArray = (GeneralItem[]) giAdap.query(runId, GeneralItemAdapter.NOT_INITIALISED);
		// String userRoles = db.getRunAdapter().queryRoles(runId);
		for (int i = 0; i < giArray.length; i++) {
			// boolean playerHasRequiredRole = false;
			// if (giArray[i].getRoles() != null &&
			// !giArray[i].getRoles().isEmpty()) {
			//
			// if (userRoles != null && !"".equals(userRoles)) {
			// for (String giRole : giArray[i].getRoles()) {
			// if (userRoles.contains(giRole))
			// playerHasRequiredRole = true;
			// }
			// }
			// } else {
			// playerHasRequiredRole =true;
			// }
			if (itemMatchesPlayersRole(db, runId, giArray[i])) {
				Dependency dep = giArray[i].getDependsOn();
				if (dep == null) {
					giAdap.setVisiblityStatus(runId, giArray[i].getId(), GeneralItemAdapter.VISIBLE, System.currentTimeMillis());
				} else {
					giAdap.setVisiblityStatus(runId, giArray[i].getId(), GeneralItemAdapter.NOT_YET_VISIBLE, System.currentTimeMillis());
				}
			}
		}
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
		boolean playerHasRequiredRole = false;
		
		if (gi.getRoles() != null && !gi.getRoles().isEmpty()) {
			String userRoles = db.getRunAdapter().queryRoles(runId);
			
			if (userRoles != null && !"".equals(userRoles)) {
				try {
					JSONArray userRolesJson = new JSONArray(userRoles);
					for (String giRole : gi.getRoles()) {
						if (containsRole(userRolesJson, giRole))
							playerHasRequiredRole = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		} else {
			playerHasRequiredRole = true;
		}
		return playerHasRequiredRole;
	}

	public void processItemsNotYetVisible(DBAdapter db, final long runId, List<Action> actions) {
		GeneralItemAdapter giAdap = ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER));
		final GeneralItem[] giArray = (GeneralItem[]) giAdap.query(runId, GeneralItemAdapter.NOT_YET_VISIBLE);
		for (int i = 0; i < giArray.length; i++) {
			if (!threads.containsKey(runId + "*" + giArray[i].getId())) {
				Dependency dep = giArray[i].getDependsOn();
				final long satisfiedAt = dep.satisfiedAt(actions);
				final long itemId = giArray[i].getId();
				final GeneralItem generalItem = giArray[i];
				if (satisfiedAt != -1) {
					long currentTime = System.currentTimeMillis();
					long satisfiedAtDelta = currentTime - satisfiedAt;
					if (satisfiedAtDelta > 0) {
						giAdap.setVisiblityStatus(runId, itemId, GeneralItemAdapter.VISIBLE, satisfiedAt);
						broadcastTroughIntent(generalItem);
					} else {
						final String threadId = runId + "*" + giArray[i].getId();
						Runnable runnable = new Runnable() {

							public void run() {
								try {
									long sleepTime = satisfiedAt - System.currentTimeMillis();
									if (sleepTime < 0)
										sleepTime = 10;
									Thread.sleep(sleepTime);
									DBAdapter db2 = new DBAdapter(ctx);
									db2.openForWrite();
									GeneralItemAdapter giAdap2 = ((GeneralItemAdapter) db2.table(DBAdapter.GENERALITEM_ADAPTER));
									giAdap2.setVisiblityStatus(runId, itemId, GeneralItemAdapter.VISIBLE, satisfiedAt);
									db2.close();

									broadcastTroughIntent(generalItem);

								} catch (InterruptedException e) {

								}
								threads.remove(threadId);

							}

						};
						threads.put(threadId, runnable);
						new Thread(runnable).start();

					}
				}
			}
		}
	}

	public void broadcastTroughIntent(GeneralItem gi) {
		if (gi.getAutoLaunch() != null && gi.getAutoLaunch()) {
			DBAdapter db = new DBAdapter(ctx);
			db.openForRead();
			gi = ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).queryById(gi.getId(), PropertiesAdapter.getInstance(ctx).getCurrentRunId());
			db.close();
			if (gi != null)
				GIActivitySelector.startActivity(ctx, gi, true);
		} else {
			Intent updateIntent = new Intent();
			updateIntent.setAction("org.celstec.arlearn.updateActivities");
			updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
			updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
			updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
			ctx.sendBroadcast(updateIntent);
		}

		if (gi != null)
			vibrateRingPhone();

	}

	public static long lastVibration = System.currentTimeMillis();

	public void vibrateRingPhone() {
		if (!beep) return;
		long now = System.currentTimeMillis();
		if (now - lastVibration < 1500)
			return;
		lastVibration = now;
		System.out.println("before play sound");
		playSound(1);
		System.out.println("after play sound");
		Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200, 200, 500, 200, 200 }, -1);

	}

	public void playSound(final int sound) {
		AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		final float volume = streamVolumeCurrent / streamVolumeMax;
		System.out.println("in play sound");
		/* Play the sound with the correct volume */

		Thread waitThread = new Thread() {
			public void run() {
				
				int counter = 0;
				try {
					System.out.println("before while "+counter + " "+ soundPoolLoaded+" " + (!soundPoolLoaded && counter < 20));
					while (!soundPoolLoaded && counter < 20) {
						sleep(200);
						counter++;
						System.out.println("in while "+counter + " "+ soundPoolLoaded+" " + (!soundPoolLoaded && counter < 20));

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			}
		};
		waitThread.run();
		

		System.out.println("in play sound");
	}
}
