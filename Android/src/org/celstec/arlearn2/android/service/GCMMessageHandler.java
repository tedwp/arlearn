package org.celstec.arlearn2.android.service;


import java.util.HashMap;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.SplashScreenActivity;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;

public class GCMMessageHandler extends GCMHandler {

	private HashMap<String, String> map;

	public GCMMessageHandler(Context ctx, HashMap<String, String> map) {
		super(ctx);
		this.map = map;
	}

	@Override
	public void handle() {
		System.out.println("process map " + map.toString());
		String messageBody = map.get("messageBody");
		String title = map.get("title");
//		NotificationManager notificationManager = (NotificationManager) ctx
//				.getSystemService(ctx.NOTIFICATION_SERVICE);
//
//		Notification myNotification = new Notification(R.drawable.icon, title,
//				System.currentTimeMillis());
//
//		// .setContentTitle("New mail from " + "test@gmail.com")
//		// .setContentText("Subject")
//		// .setSmallIcon(R.drawable.icon)
//		// .setContentIntent(pIntent)
//		// .addAction(R.drawable.icon, "Call", pIntent)
//		// .addAction(R.drawable.icon, "More", pIntent)
//		// .addAction(R.drawable.icon, "And more", pIntent).build();
//		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//
//		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
//				myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		myNotification.defaults |= Notification.DEFAULT_SOUND;
//		myNotification.setLatestEventInfo(ctx, title, messageBody,
//				pendingIntent);
//
//		notificationManager.notify(0, myNotification);
		
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
			.setSmallIcon(R.drawable.arlearn_icon_48)
		        .setContentTitle(title)
		        .setContentText(messageBody);
		
		Intent resultIntent = new Intent(ctx, SplashScreenActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
		stackBuilder.addParentStack(SplashScreenActivity.class);
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
//		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(123, mBuilder.build());
	}

}
