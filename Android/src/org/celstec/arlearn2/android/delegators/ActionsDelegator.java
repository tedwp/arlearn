package org.celstec.arlearn2.android.delegators;

import java.util.Iterator;

import org.celstec.arlearn2.android.broadcast.task.PublishActionTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;

import android.content.Context;
import android.os.Message;

public class ActionsDelegator {

	private static ActionsDelegator instance;
	
	private ActionsDelegator() {
		
	}
	
	public static ActionsDelegator getInstance() {
		if (instance == null) {
			instance = new ActionsDelegator();
		}
		return instance;
	}
	
	public void publishAction(Context ctx, String actionString, Long runId, String userEmail) {
		publishAction(ctx, actionString, runId, userEmail, null, null);
	}
	
	public void publishAction(Context ctx, String actionString, Long runId, String userEmail, Long generalItemId, String generalItemType) {
		Action action = new Action();
		action.setAction(actionString);
		action.setRunId(runId);
		action.setUserEmail(userEmail);
		action.setGeneralItemId(generalItemId);
		action.setGeneralItemType(generalItemType);
		action.setTime(System.currentTimeMillis());
		publishAction(ctx, action);
	}
	
	public void publishStartRunActon(Context ctx, Long runId, String userEmail) {
		Action action = new Action();
		action.setAction("startRun");
		action.setRunId(runId);
		action.setUserEmail(userEmail);

		action.setTime(System.currentTimeMillis());
		publishAction(ctx, action);
	}
	
	public void publishAction(final Context ctx, final Action action) {
		DBAdapter.DatabaseTask pa = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				db.getMyActions().insert(action, false);
				for (Action action: db.getMyActions().queryActionsNotReplicated()) {
					(new PublishActionTask(db.getContext(), action)).addTaskToQueue(db.getContext());
				}
				dependencyCheck(ctx);
			}
		};
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = pa;
		m.sendToTarget();
	}
	
	public void confirmReplicated(Context ctx, final Action action) {
		DBAdapter.DatabaseTask task = new DBAdapter.DatabaseTask() {
			
			@Override
			public void execute(DBAdapter db) {
				db.getMyActions().confirmReplicated(action.getTime(), action.getAction());
			}
		};
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = task;
		m.sendToTarget();
	}
	
	private void dependencyCheck(Context ctx) {
		(new GeneralItemDependencyHandler()).addTaskToQueue(ctx);
	}
	
	public void saveServerActionsToAndroidDb(Context ctx, final ActionList al, final String userName) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj =  new DBAdapter.DatabaseTask () {
			@Override
			public void execute(DBAdapter db) {
				Iterator<Action> it = al.getActions().iterator();
				while (it.hasNext()) {
					Action a = it.next();

					if (a != null && userName != null && userName.equals(a.getUserEmail())) {
						db.getMyActions().insert(a, true);
					} 	
				}
				(new GeneralItemDependencyHandler()).addTaskToQueue(db.getContext());
			}
		};
		m.sendToTarget();
	}
}
