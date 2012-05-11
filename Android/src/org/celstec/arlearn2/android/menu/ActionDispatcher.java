package org.celstec.arlearn2.android.menu;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.asynctasks.PublishActionTask;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.beans.run.Action;

import android.content.Context;
import android.content.Intent;


public class ActionDispatcher {

	
	public static void startRun(IGeneralActivity activity) {
		Action action = new Action();
		action.setAction("startRun");
		action.setRunId(activity.getMenuHandler().getPropertiesAdapter().getCurrentRunId());
		action.setUserEmail(activity.getMenuHandler().getPropertiesAdapter().getUsername());

		action.setTime(System.currentTimeMillis());
		PublishActionTask task = new PublishActionTask(activity);
		task.execute(new Object[] {action});
		Context ctx = (Context) activity;

		Intent intent = new Intent(ctx, BackgroundService.class);
		intent.putExtra("depCheck", true);
		ctx.startService(intent);
	}
	
	public static void publishAction(IGeneralActivity activity, String actionString, Long runId, String userEmail) {
		publishAction(activity, actionString, runId, userEmail, null, null);
	}
	
	public static void publishAction(IGeneralActivity activity, String actionString, Long runId, String userEmail, Long generalItemId, String generalItemType) {
		Action action = new Action();
		action.setAction(actionString);
		action.setRunId(runId);
		action.setUserEmail(userEmail);
		action.setGeneralItemId(generalItemId);
		action.setGeneralItemType(generalItemType);
		action.setTime(System.currentTimeMillis());
		publishAction(activity, action);
		
	}
	
	public static void publishAction(IGeneralActivity activity, Action action) {
		Context ctx = (Context) activity;
		PublishActionTask task = new PublishActionTask(activity);
		task.execute(new Object[] {action});
		
	}

}
