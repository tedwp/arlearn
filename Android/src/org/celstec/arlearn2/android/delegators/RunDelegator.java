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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.db.LoadMediaUploadToCache;
import org.celstec.arlearn2.android.asynctasks.db.LoadRunsAndGamesToCache;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeRunsTask;
import org.celstec.arlearn2.android.asynctasks.network.UnregisterRunTask;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;

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
	
	//TODO this method is never called
	public void initRunsAndGamesFromDb(Context ctx) {
		LoadRunsAndGamesToCache gameAndRunsTask = new LoadRunsAndGamesToCache();
		gameAndRunsTask.run(ctx);
	}
	
	public void initRunsAndGamesFromDb(Context ctx, String activityToUpdate) {
		LoadRunsAndGamesToCache gameAndRunsTask = new LoadRunsAndGamesToCache(new String[]{activityToUpdate});
		gameAndRunsTask.run(ctx);
	}
	
	public void synchronizeRunsWithServer(Context ctx) {
		(new SynchronizeRunsTask(ctx)).run(ctx);
	}
	
	public void saveServerRunsToAndroidDb(Context ctx, final RunList rl) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				if (rl.getError() == null) {
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
						boolean runDeleted = false;
						if (run.getDeleted() != null) {
							runDeleted = run.getDeleted();
						}
						db.getRunAdapter().insertBeta(run);
						if (!runDeleted) {
							UserDelegator.getInstance().synchronizeUserWithServer(db.getContext(), run.getRunId(), PropertiesAdapter.getInstance(db.getContext()).getUsername());
							GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(db.getContext(), run.getRunId(), run.getGameId());
							GeneralItemsDelegator.getInstance().initializeGeneralItemsVisibility(db, run.getRunId(), run.getGameId());
						}
						RunCache.getInstance().put(run);
					}

				}
			}
		}
		ActivityUpdater.updateActivities(db.getContext(), ListRunsParticipateActivity.class.getCanonicalName());
	}
	
	public void unregisterRun(Context ctx, long runId){
		(new UnregisterRunTask(runId)).run(ctx);
		
	}
	
	public Run[] getRuns() {
		Run[] runs = RunCache.getInstance().getRuns();
		return runs;
	}
	
	public Run getRun(long runId) {
		return  RunCache.getInstance().getRun(runId);
	}
	
	public void loadRun(Context ctx, Long runId) {
		Run r = RunCache.getInstance().getRun(runId);
		if (r != null) {
			(new LoadMediaUploadToCache(runId)).run(ctx);
			GameDelegator.getInstance().loadGameToCache(ctx, r.getGameId());
			DownloadFileTask syncTask = new DownloadFileTask();
			syncTask.gameId = r.getGameId();
			syncTask.run(ctx);
		}
	}
	
}
