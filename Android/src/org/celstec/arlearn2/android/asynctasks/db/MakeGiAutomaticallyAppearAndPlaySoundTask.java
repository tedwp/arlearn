package org.celstec.arlearn2.android.asynctasks.db;

import java.util.HashMap;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemVisibilityDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Message;
import android.os.Vibrator;

public class MakeGiAutomaticallyAppearAndPlaySoundTask extends GenericTask implements DatabaseTask {

	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static boolean soundPoolLoaded = false;
	private static long lastSoundPlayed = 0l;
	private static long lastVibratePlayed = 0l;
	
	

	private GeneralItem gi;
	// private static boolean beep = true;
	private long itemId;
	private int status;

	public MakeGiAutomaticallyAppearAndPlaySoundTask(long itemId, int status) {
		this.itemId = itemId;
		this.status = status;
	}

	@Override
	public void execute(DBAdapter db) {
		if (soundPool == null) {
			initSoundPool(db.getContext());
		}
		gi = GeneralItemsDelegator.getInstance().getGeneralItem(db, itemId);
		if (gi != null && !gi.isDeleted()) {
			if (appearCondition() || disappearCondition()) {
				if (appearCondition()) {
					checkAutoLaunch(db.getContext());
					playSound(1, db.getContext());
					vibrate(db.getContext());
				}
				if (disappearCondition()) {
					vibrateShort(db.getContext());
				}
				ActivityUpdater.updateActivities(db.getContext(), ListMessagesActivity.class.getCanonicalName(), MapViewActivity.class.getCanonicalName(), ListMapItemsActivity.class.getCanonicalName(), NarratorItemActivity.class.getCanonicalName());
			}
		}
		runAfterTasks(db.getContext());
	}

	private boolean appearCondition() {
		return (gi.getDependsOn() != null && status == GeneralItemVisibility.VISIBLE);
	}

	private boolean disappearCondition() {
		return (gi.getDisappearOn() != null && status == GeneralItemVisibility.NO_LONGER_VISIBLE);
	}

	@Override
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
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

	public void checkAutoLaunch(Context ctx) {
		if (gi.getAutoLaunch() != null && gi.getAutoLaunch()) {

			ActivityUpdater.closeActivities(ctx, gi.getId(), NarratorItemActivity.class.getCanonicalName());
			GIActivitySelector.startActivity(ctx, gi, true);

		}
	}

	private void vibrate(Context ctx) {
		if ((System.currentTimeMillis()-lastVibratePlayed)< 1500) return;
		Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200, 200, 500, 200, 200 }, -1);
		lastVibratePlayed = System.currentTimeMillis();
	}
	private void vibrateShort(Context ctx) {
		if ((System.currentTimeMillis()-lastVibratePlayed)< 1500) return;
		Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200 }, -1);
		lastVibratePlayed = System.currentTimeMillis();
	}

	public static void playSound(final int sound, Context ctx) {
		if ((System.currentTimeMillis()-lastSoundPlayed)< 2000) return;
		AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		final float volume = streamVolumeCurrent / streamVolumeMax;
		if (soundPoolLoaded) {
			soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			lastSoundPlayed = System.currentTimeMillis();
		} else {
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
	}

}
