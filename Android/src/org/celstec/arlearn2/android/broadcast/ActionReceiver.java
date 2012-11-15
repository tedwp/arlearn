package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeActionsTask;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.client.ActionClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class ActionReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.Action";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			saveAction(extras.getString("action"), context);
		} else {
			(new SynchronizeActionsTask()).addTaskToQueue(context);		
		}
	}
	
	private void saveAction(String action, Context context) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(context);
		Action a = new Action();
		a.setAction(action);
		if (pa.getUsername() != null && pa.getCurrentRunId()!= -1) {
			a.setUserEmail(pa.getUsername());
			a.setRunId(pa.getCurrentRunId());
			a.setTime(System.currentTimeMillis());
			Action result = ActionClient.getActionClient().publishAction(pa.getFusionAuthToken(), a);
		}
	}

}
