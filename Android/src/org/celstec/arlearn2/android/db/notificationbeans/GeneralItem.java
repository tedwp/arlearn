package org.celstec.arlearn2.android.db.notificationbeans;

import java.util.HashMap;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class GeneralItem extends NotificationBean{
	private Long itemId;
	private String action;
	private String name;
	
	private Long runId;
	
	private Double lat;
	private Double lng;
	
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	public boolean requiresBroadcast() {
//		if (lat == null  || lat == 0l) {
//			if (lng == null  || lng == 0l)  return false;
//		}
		return true;
	}
	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;

	@Deprecated
	public void run(Context ctx) {
		if ("visible".equals(getAction())) {
			DBAdapter db = new DBAdapter(ctx);
			db.openForWrite();
//			((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).setDependsOnVisible(getItemId());
			((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).setVisiblityStatus(getRunId(), getItemId(), GeneralItemAdapter.VISIBLE, System.currentTimeMillis());
			db.close();
			GeneralItemsSyncroniser.syncronizeItems(ctx, new PropertiesAdapter(ctx));
//			GeneralItemsSyncroniser.syncronizeItems(ctx, new PropertiesAdapter(ctx));
			Vibrator vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
	        vibrator.vibrate(new long[] {0, 200,200, 500,200,200}, -1);
	        playSound(1, ctx);
		}

	}
	
	public static void playSound(int sound, Context ctx) {
		
		if (soundPool == null) {
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	        soundPoolMap = new HashMap<Integer, Integer>();
	        soundPoolMap.put(1, soundPool.load(ctx, R.raw.multi_new, 1));
		}
	    /* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
	    AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
	    float volume = streamVolumeCurrent / streamVolumeMax;
	    
	    /* Play the sound with the correct volume */
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);     
	}
	
}
