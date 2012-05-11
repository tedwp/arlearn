package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

public class AlarmService extends IntentService {
	private static int icon = R.drawable.arlearn_logo;

	public AlarmService() {
		super("AlarmService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeneralItem item = (GeneralItem) intent.getExtras().getSerializable("generalItem");
		Run run = (Run) intent.getExtras().getSerializable("run");
		if (item.isMessage()) {
//			handleMessage(item.getId(), run);
		} else {
//			handleLocationObject(item.getId(), run);
		}
	}
//
//	private void handleLocationObject(Long itemId, Run run) {
//		DBAdapter db = new DBAdapter(this);
//		db.openForWrite();
//		final GeneralItem item = getItem(db, itemId);
////		if (item.isDependsOnVisible()) {
//			GeneralItemAdapter giAdapter = (GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER);
//
//			giAdapter.setTimeVisible(itemId, GeneralItemAdapter.VISIBLE);
//			giAdapter.setFirstRead(itemId, System.currentTimeMillis());
//			
//			CharSequence tickerText = "Message available";
//			final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//			Intent intent = new Intent(NotificationService.BROADCAST_ACTION);
//			org.celstec.arlearn2.android.db.notificationbeans.GeneralItem bean = new org.celstec.arlearn2.android.db.notificationbeans.GeneralItem();
//			bean.setItemId(item.getId());
//			bean.setAction("visible");
//			intent.putExtra("bean", bean);
//	    	sendBroadcast(intent);
//	    	
////			final Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
////			CharSequence contentTitle = item.getName();
////			CharSequence contentText = item.getDescription();
////			Intent notificationIntent = new Intent(this, item.getCorrespondingActivity());
////			notificationIntent.putExtra(GeneralItem.ID, item.getId());
////			notificationIntent.putExtra("generalItem", item);
////
////			PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
////			notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
////			notification.sound = Uri.parse("android.resource://org.celstec.arlearn2.android/" + R.raw.activity);
////			mNotificationManager.notify(item.getAutoId(), notification);
////		}
//		db.close();
//	}
//
//	private void handleMessage(Long itemId, Run run) {
//		DBAdapter db = new DBAdapter(this);
//		db.openForWrite();
//		final GeneralItem item = getItem(db, itemId);
////		if (item.isDependsOnVisible()) {
//			GeneralItemAdapter giAdapter = (GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER);
//
//			giAdapter.setTimeVisible(itemId, GeneralItemAdapter.VISIBLE);
//			giAdapter.setFirstRead(itemId, System.currentTimeMillis());
//			
//			CharSequence tickerText = "Message available";
//			final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//			Intent intent = new Intent(NotificationService.BROADCAST_ACTION);
//			org.celstec.arlearn2.android.db.notificationbeans.GeneralItem bean = new org.celstec.arlearn2.android.db.notificationbeans.GeneralItem();
//			bean.setItemId(item.getId());
//			bean.setAction("visible");
//			intent.putExtra("bean", bean);
//	    	sendBroadcast(intent);
//	    	
//			final Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
//			CharSequence contentTitle = item.getName();
//			CharSequence contentText = item.getDescription();
//			
//			Intent notificationIntent = new Intent(this, GIActivitySelector.getCorrespondingActivity(item));
//			notificationIntent.putExtra("id", item.getId()); //TODO make constant
//			notificationIntent.putExtra("generalItem", item);
//
//			PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//			notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
//			notification.sound = Uri.parse("android.resource://org.celstec.arlearn2.android/" + R.raw.activity);
//			mNotificationManager.notify((int) item.getId().longValue(), notification);
////		}
//		db.close();
//	}
//
//	public GeneralItem getItem(DBAdapter db, Long itemId) {
//		return (GeneralItem) db.table(DBAdapter.GENERALITEM_ADAPTER).queryById(itemId);
//	}

}
