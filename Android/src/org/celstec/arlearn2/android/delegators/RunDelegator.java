package org.celstec.arlearn2.android.delegators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeRunsTask;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;

import android.content.ContentValues;
import android.content.Context;
import android.os.Message;

public class RunDelegator {
	
	private static RunDelegator instance;
	
	private RunDelegator() {
		
	}
	
	public static RunDelegator getInstance() {
		if (instance == null) {
			instance = new RunDelegator();
		}
		return instance;
	}
	
	public void synchronizeRunsWithServer(Context ctx) {
		(new SynchronizeRunsTask(ctx)).addTaskToQueue(ctx);
	}
	
	public void saveServerRunsToAndroidDb(Context ctx, final RunList rl) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				if (rl.getError() == null) {
//					db.getRunAdapter().insert(rl.getRuns());
					saveRunsToAndroidDb(db, rl.getRuns());
				}

			}
		};
		m.sendToTarget();
	}
	
	public void saveServerRunToAndroidDb(Context ctx, final Run run) {
		List<Run> runs = new ArrayList<Run>();
		runs.add(run);
		RunList rl = new RunList();
		rl.setRuns(runs);
		saveServerRunsToAndroidDb(ctx, rl);
	}
	
	private void saveRunsToAndroidDb(DBAdapter db, List<Run> runs) {
		for (Iterator<Run> iterator = runs.iterator(); iterator.hasNext();) {
			Run run = iterator.next();
			if (run.getDeleted() != null && run.getDeleted()) {
				db.getRunAdapter().delete(run.getRunId());
			} else {
				Run cachedRun = RunCache.getInstance().getRun(run.getRunId());
				if (!run.equals(cachedRun)) {
					Run oldRun = (Run) db.getRunAdapter().queryById(run.getRunId());
					if (oldRun != null) {
						if (!oldRun.equals(run)) {
							db.getRunAdapter().update(run, null);
						} else {
							RunCache.getInstance().put(run);
						}
					} else {
						System.out.println("hier");
						boolean runDeleted = false;
						if (run.getDeleted() != null) {
							runDeleted = run.getDeleted();
						}
						System.out.println("hier");
						db.getRunAdapter().insertBeta(run);
						System.out.println("hier");
						if (!runDeleted) {
							UserDelegator.getInstance().synchronizeUserWithServer(db.getContext(), run.getRunId(), PropertiesAdapter.getInstance(db.getContext()).getUsername());
							GameDelegator.getInstance().synchronizeGameWithServer(db.getContext(), run.getGameId());
						}
						RunCache.getInstance().put(run);
					}

				}
			}
		}
		ActivityUpdater.updateActivities(db.getContext(), ListExcursionsActivity.class.getCanonicalName());
	}
	
	public Run[] getRuns() {
		Run[] runs = RunCache.getInstance().getRuns();
		return runs;
	}
	
}
