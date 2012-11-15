package org.celstec.arlearn2.android.broadcast;

import java.io.Serializable;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
					reCast(RunReceiver.action, bean, context);
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
