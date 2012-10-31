package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.android.sync.MyActionsSyncronizer;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.client.ActionClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("action received");
		Bundle extras = intent.getExtras();
		if (extras != null) {
			String action = extras.getString("action");
			PropertiesAdapter pa = PropertiesAdapter.getInstance(context);
			Action a = new Action();
			a.setAction(action);
			if (pa.getUsername() != null && pa.getCurrentRunId()!= -1) {
				a.setUserEmail(pa.getUsername());
				a.setRunId(pa.getCurrentRunId());
				a.setTime(System.currentTimeMillis());
//				publishAction(context, a);
				Action result = ActionClient.getActionClient().publishAction(pa.getFusionAuthToken(), a);

			}

		}
	}
	
//	private void publishAction(Context context, Action action) {
//		DBAdapter db = new DBAdapter(context);
//		db.openForWrite();
//		((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).insert(action);
//		db.close();
//		if (MyActionsSyncronizer.getInstance() != null) MyActionsSyncronizer.getInstance().run();
//		Intent intent = new Intent(context, BackgroundService.class);
//		intent.putExtra("depCheck", true);
//		context.startService(intent);
//
//	}

}
