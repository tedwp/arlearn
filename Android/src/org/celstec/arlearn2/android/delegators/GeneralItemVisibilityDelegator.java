package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.broadcast.VisibilityAlarmReceiver;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class GeneralItemVisibilityDelegator {

	private static GeneralItemVisibilityDelegator instance;

	private GeneralItemVisibilityDelegator() {

	}

	public static GeneralItemVisibilityDelegator getInstance() {
		if (instance == null) {
			instance = new GeneralItemVisibilityDelegator();
		}
		return instance;
	}

	public void makeItemVisible(DBAdapter db, long itemId, Long runId, long satisfiedAt, int status) {
		Context ctx = db.getContext();
		long oldSatisfiedAt = db.getGeneralItemVisibility().query(itemId, runId, status);
		if (oldSatisfiedAt != -1) {
			if (oldSatisfiedAt < System.currentTimeMillis()) return;
			if (oldSatisfiedAt < satisfiedAt) return;
		}
		String action = null;
		AlarmManager alarmManager = null;
		Intent alarmIntent = null;
		if ((status == GeneralItemVisibility.VISIBLE || status == GeneralItemVisibility.NO_LONGER_VISIBLE) && satisfiedAt != 0) {
			action = VisibilityAlarmReceiver.ACTION;
		}
		if (action != null) {
			alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
			alarmIntent = new Intent(ctx, VisibilityAlarmReceiver.class);
			alarmIntent.setData(Uri.parse("arlearn://visibility/" + itemId + "/" + runId + "/" + status + "/" + oldSatisfiedAt));
			alarmIntent.setAction(action);
			if (oldSatisfiedAt == -1 || oldSatisfiedAt > satisfiedAt) {
				PendingIntent displayIntent = PendingIntent.getBroadcast(db.getContext(), 0, alarmIntent, 0);
				alarmManager.cancel(displayIntent);
			}
		}
		if (oldSatisfiedAt == -1 || oldSatisfiedAt > satisfiedAt) {
			db.getGeneralItemVisibility().delete(itemId, runId, satisfiedAt, status);
			db.getGeneralItemVisibility().setVisibilityStatus(itemId, runId, satisfiedAt, status);
			if (action != null) {
				alarmIntent.setData(Uri.parse("arlearn://visibility/" + itemId + "/" + runId + "/" + status + "/" + satisfiedAt));

				alarmIntent.putExtra(VisibilityAlarmReceiver.ITEM_ID, itemId);
				alarmIntent.putExtra(VisibilityAlarmReceiver.RUN_ID, runId);
				alarmIntent.putExtra(VisibilityAlarmReceiver.STATUS, status);
				if (status == GeneralItemVisibility.VISIBLE) {
					alarmIntent.putExtra(VisibilityAlarmReceiver.APPEAR_AT, satisfiedAt);
				}
				if (status == GeneralItemVisibility.NO_LONGER_VISIBLE) {
					alarmIntent.putExtra(VisibilityAlarmReceiver.DISAPPEAR_AT, satisfiedAt);
				}
				PendingIntent displayIntent = PendingIntent.getBroadcast(db.getContext(), 0, alarmIntent, 0);
				alarmManager.set(AlarmManager.RTC, satisfiedAt, displayIntent);
				Log.i("VISIBLE", "making visible " + itemId + " " + runId + " " + status + " at " + satisfiedAt);
			}
		}

	}

}
