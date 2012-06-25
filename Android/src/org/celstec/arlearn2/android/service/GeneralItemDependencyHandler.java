package org.celstec.arlearn2.android.service;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.android.R;
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

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class GeneralItemDependencyHandler {

	private Context ctx;
	private HashMap<String, Runnable> threads = new HashMap<String, Runnable>();
	private  SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	
	public GeneralItemDependencyHandler(Context ctx) {
		this.ctx = ctx;
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, soundPool.load(ctx, R.raw.multi_new, 1));
	}
	
	public synchronized void checkDependencies() {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		long runId = (new PropertiesAdapter(ctx)).getCurrentRunId();
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
//		String userRoles = db.getRunAdapter().queryRoles(runId);
		for (int i = 0; i < giArray.length; i++) {
//			boolean playerHasRequiredRole = false;
//			if (giArray[i].getRoles() != null && !giArray[i].getRoles().isEmpty()) {
//				
//				if (userRoles != null && !"".equals(userRoles)) {
//					for (String giRole : giArray[i].getRoles()) {
//						if (userRoles.contains(giRole))
//							playerHasRequiredRole = true;
//					}
//				}
//			} else {
//				playerHasRequiredRole =true;
//			}
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
	
	public static boolean itemMatchesPlayersRole(DBAdapter db, long runId, GeneralItem gi ) {
		boolean playerHasRequiredRole = false;
		String userRoles = db.getRunAdapter().queryRoles(runId);
		if (gi.getRoles() != null && !gi.getRoles().isEmpty()) {
			
			if (userRoles != null && !"".equals(userRoles)) {
				for (String giRole : gi.getRoles()) {
					if (userRoles.contains(giRole))
						playerHasRequiredRole = true;
				}
			}
		} else {
			playerHasRequiredRole =true;
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
				if (satisfiedAt != -1) {				
					long currentTime = System.currentTimeMillis();
					long satisfiedAtDelta = currentTime - satisfiedAt; 
					if (satisfiedAtDelta > 0) {
						giAdap.setVisiblityStatus(runId, itemId, GeneralItemAdapter.VISIBLE, satisfiedAt);
						org.celstec.arlearn2.android.db.notificationbeans.GeneralItem giNot = new org.celstec.arlearn2.android.db.notificationbeans.GeneralItem();
						giNot.setItemId(giArray[i].getId());
						giNot.setRunId(runId);
						giNot.setAction("visible");
						broadcastTroughIntent(giNot);
					} else {
						final String threadId = runId + "*" + giArray[i].getId();
						Runnable runnable = new Runnable() {

							public void run() {
								try {
									long sleepTime = satisfiedAt - System.currentTimeMillis();
									if (sleepTime < 0) sleepTime = 10;
									Thread.sleep(sleepTime);
									DBAdapter db2 = new DBAdapter(ctx);
									db2.openForWrite();
									GeneralItemAdapter giAdap2 = ((GeneralItemAdapter) db2.table(DBAdapter.GENERALITEM_ADAPTER));
									giAdap2.setVisiblityStatus(runId, itemId, GeneralItemAdapter.VISIBLE, satisfiedAt);
									db2.close();
									
									
									
									org.celstec.arlearn2.android.db.notificationbeans.GeneralItem giNot = new org.celstec.arlearn2.android.db.notificationbeans.GeneralItem();
									giNot.setItemId(itemId);
									
									giNot.setRunId(runId);
									giNot.setAction("visible");
									broadcastTroughIntent(giNot);
									
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
	
	private  void broadcastTroughIntent(org.celstec.arlearn2.android.db.notificationbeans.GeneralItem giNot) {
//		Intent intent = new Intent(NotificationService.BROADCAST_ACTION);
//		intent.putExtra("bean", giNot);
//		ctx.sendBroadcast(intent);
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
		ctx.sendBroadcast(updateIntent);
		vibrateRingPhone();
		
	}
	
	public void vibrateRingPhone(){
		Vibrator vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] {0, 200,200, 500,200,200}, -1);
        playSound(1);
	}
	
	public void playSound(int sound) {
	    /* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
	    AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
	    float volume = streamVolumeCurrent / streamVolumeMax;
	    
	    /* Play the sound with the correct volume */
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);     
	}
}
