package org.celstec.arlearn2.android.sync;

import java.util.Iterator;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.android.service.AlarmService;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GeneralItemsSyncroniser extends GenericSyncroniser {

	private boolean runNextTime = false;

	
	public GeneralItemsSyncroniser(Context ctx) {
		super(ctx);
	}

	public void runAuthenticated() {

		PropertiesAdapter pa = new PropertiesAdapter(ctx);
		if (pa.getCurrentRunId() != -1) {
			boolean increase = syncronizeItems(ctx, pa);
			if (increase) {
				increaseDelay();
			} else {
				resetDelay();
			}
		}

	}

	public static boolean syncronizeItems(Context ctx, PropertiesAdapter pa) {
		return syncronizeItems(ctx, pa, pa.getCurrentRunId());
	}
	
	public static boolean syncronizeItems(Context ctx, PropertiesAdapter pa, Long currentRunId) {
		boolean returnValue = true;
		if (currentRunId == 0) return true;
		try {
			GeneralItemClient gic = GeneralItemClient.getGeneralItemClient();
			GeneralItemList gl = gic.getRunGeneralItems(pa.getFusionAuthToken(), currentRunId);
			DBAdapter db = new DBAdapter(ctx);
			db.openForWrite();
			if (gl.getErrorCode() != null && gl.getErrorCode() == GeneralItemList.RUNNOTFOUND) {
				db.deleteRun(currentRunId);
			}
			Run currentRun = (Run) ((RunAdapter) db.table(DBAdapter.RUN_ADAPTER)).queryById(currentRunId);
			Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
			while (it.hasNext()) {
				GeneralItem item = it.next();
				item.setRunId(currentRunId);
				boolean newInsert = db.table(DBAdapter.GENERALITEM_ADAPTER).insert(item);
				if (newInsert)
					processItemForAdditionalWork(item, db);

			}
//			timeInitGeneralItems(db, ctx, currentRun);
			
			db.close();
			return returnValue;
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			return true;
		}
	}
	
	private static void processItemForAdditionalWork(GeneralItem item, DBAdapter db) {
		if (item instanceof AudioObject) {
			AudioObject aItem = (AudioObject) item;
			((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(aItem.getId(), aItem.getAudioFeed(), aItem.getRunId());
			MediaCacheSyncroniser.getInstance().resetDelay();
		}
		if (item instanceof VideoObject) {
			VideoObject vItem = (VideoObject) item;
			((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(vItem.getId(), vItem.getVideoFeed(), vItem.getRunId());
			MediaCacheSyncroniser.getInstance().resetDelay();
		}
		
	}

//	public static void timeInitGeneralItems(DBAdapter db, Context ctx, Run currentRun){
//		GeneralItemAdapter giAdapter = (GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER);
//		MyActions myActions = (MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER);
//		myActions.unblockVisibleItems(currentRun.getRunId());
//		GeneralItem[] gis = giAdapter.queryNotTimeInitialised(currentRun.getRunId());
//		for (int i = 0; i < gis.length; i++) {
//			giAdapter.setTimeVisible(gis[i].getId(), GeneralItemAdapter.NOT_YET_VISIBLE);
////			myActions.checkVisibility(gis[i]);
//
//			scheduleAlarm(ctx, gis[i], currentRun);
//		}
//	}
	
	public static Run getCurrentRun(DBAdapter db, Long runId) {
		return (Run) ((RunAdapter) db.table(DBAdapter.RUN_ADAPTER)).queryById(runId);
	}
	
	public static Run getCurrentRun(Context ctx, Long runId){
		DBAdapter db = new DBAdapter(ctx);
		try {
			db.openForRead();
			return getCurrentRun(db, runId);
		} finally {
			db.close();
		}
	}
	

	public static void scheduleAlarm(Context ctx, GeneralItem item, Run run) {
		Intent alarmIntent = new Intent(ctx, AlarmService.class);
		alarmIntent.putExtra("generalItem", item);
//		if (run == null) {
//			run = getCurrentRun(ctx, item.getRunId());
//		}
		alarmIntent.putExtra("run", run);
		if (item.getShowAtTimeStamp() == 0) {
			ctx.startService(alarmIntent);
		} else {
			PropertiesAdapter pa = new PropertiesAdapter(ctx);
			long when = pa.getRunStart(run.getRunId()) + item.getShowAtTimeStamp();
			PendingIntent pi = PendingIntent.getService(ctx, 1, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
			am.cancel(pi);
			am.set(AlarmManager.RTC_WAKEUP, when, pi);
		}
	}

}
