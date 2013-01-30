package org.celstec.arlearn2.android.broadcast;

import java.io.Serializable;

import org.celstec.arlearn2.android.delegators.RunDelegator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BeanReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn.beanbroadcast";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		if (extras != null) {
			Serializable bean = extras.getSerializable("bean");
			try {
				switch (NotificationBeans.valueOf(bean.getClass().getSimpleName())) {
				case RunModification:
					RunDelegator.getInstance().synchronizeRunsWithServer(context);
//					reCast(RunReceiver.action, bean, context);
					break;
				case GeneralItemModification:
					reCast("org.celstec.arlearn2.beans.notification.GeneralItemModification", bean, context);
					break;
				case Ping:
					reCast("org.celstec.arlearn2.beans.notification.Ping", bean, context);
					break;
				default:
					break;
				}
			} catch (IllegalArgumentException e) {
				// eat this
			}
		}
	}

	private void reCast(String action, Serializable bean, Context context) {
		Intent runIntent = new Intent();
		runIntent.setAction(action);
		runIntent.putExtra("bean", bean);
		context.sendBroadcast(runIntent);
	}

	enum NotificationBeans {
		RunModification, GeneralItemModification, Ping, Pong, LocationUpdate, Action, Response;
	}

}
