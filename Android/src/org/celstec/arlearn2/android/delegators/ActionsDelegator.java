/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.delegators;

import java.util.Iterator;

import org.celstec.arlearn2.android.asynctasks.db.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.asynctasks.network.PublishActionTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeActionsTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;

import android.content.Context;
import android.os.Message;
import android.util.Log;

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
	
	public void synchronizeActionsWithServer(Context context) {
		(new SynchronizeActionsTask()).run(context);	
		DBAdapter.DatabaseTask pa = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				db.getMyActions().queryAll(db, PropertiesAdapter.getInstance(db.getContext()).getCurrentRunId());
			}
		};
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(context));
		m.obj = pa;
		m.sendToTarget();

	}
	
	public void publishAction(Context ctx, String actionString, Long runId, String userEmail, Long generalItemId, String generalItemType) {
		Log.i("VIS", "inside publish action");
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
					db.getMyActions().confirmReplicated(action.getTime(), action.getAction());
				}
				dependencyCheck(ctx);
				db.getMyResponses().syncResponses();
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
	
	public void replicationFailed(Context ctx, final Action action) {
		DBAdapter.DatabaseTask task = new DBAdapter.DatabaseTask() {
			
			@Override
			public void execute(DBAdapter db) {
				db.getMyActions().replicationFailed(action.getTime(), action.getAction());
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
