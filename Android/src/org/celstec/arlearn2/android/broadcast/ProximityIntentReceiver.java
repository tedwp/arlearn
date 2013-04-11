package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.asynctasks.db.ProcessProximityAlert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProximityIntentReceiver extends BroadcastReceiver {
		public static final String ID = "uniqueId";

	    
		@Override
		public void onReceive(Context ctx, Intent intent) {
			System.out.println(intent.getExtras());
			long id = intent.getLongExtra(ID, 0);
//			PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 12, intent, 0);
			ProcessProximityAlert ppa = new ProcessProximityAlert();
			ppa.setUniqueId(id);
			ppa.run(ctx);
		}
}
