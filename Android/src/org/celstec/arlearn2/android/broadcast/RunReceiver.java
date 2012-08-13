package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.UserClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class RunReceiver extends GenericReceiver {
	
	public static String action = "org.celstec.arlearn2.beans.notification.RunModification";
	private Semaphore semaphore;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (semaphore == null) semaphore = new Semaphore(1);
		new Thread(new Runnable() {
			public void run() {
				try {
					semaphore.acquire();

					Bundle extras = intent.getExtras();
					if (extras != null) {
						RunModification bean = (RunModification) extras.getSerializable("bean");
						Log.e("GAME", "in thread 1");
						if (bean != null) {
							// process(context, bean);
							databaseOperations(context, bean);
						} else {
							Log.e("GAME", "in thread 2 bean is null");	
						}
					} else {
						syncronizeRuns(context);
					}
					updateActivities(context);
					semaphore.release();
				} catch (InterruptedException e) {
					Log.e("ERROR", e.getMessage(), e);
				}
			}
		}).start();
	}
	
	private void databaseOperations(Context ctx, RunModification rm) {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		boolean updateGeneralItems = false;
		switch (rm.getModificationType()) {
		case RunModification.CREATED:
			Log.e("GAME", "in thread 2 CREATED");
			runToDb(db, rm.getRun(), ctx);
			updateGeneralItems = true;
			Intent gimIntent = new Intent();
			gimIntent.putExtra("runId", rm.getRun().getRunId());
			gimIntent.setAction(GeneralItemReceiver.action);
			ctx.sendBroadcast(gimIntent);
			break;
		case RunModification.DELETED:
			Log.e("GAME", "in thread 2 DELETED");

			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			break;
		case RunModification.ALTERED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
			updateGeneralItems = true;
			updateGameAndUser(ctx, db, rm.getRun());
			break;
		default:
			break;
		}
		db.close();
		if (updateGeneralItems) {
			Intent gimIntent = new Intent();
			gimIntent.setAction(GeneralItemReceiver.action);
			ctx.sendBroadcast(gimIntent);
		}
	}
	
	public  void syncronizeRuns(Context ctx) {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		try { 
			RunList rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			if (rl.getError() == null) {
				Iterator<Run> it = rl.getRuns().iterator();
				while (it.hasNext()) {
					Run run = it.next();
					runToDb(db, run, ctx);
				}
//				((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rl.getRuns());
			}
		} catch (ARLearnException ae){
			if (ae.invalidCredentials()) {
				setStatusToLogout(ctx);
			}
			
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			
		} finally {
			db.close();
		}		
	}
	
	private void runToDb(DBAdapter db, Run run, Context ctx){
		Log.e("GAME", "in thread 3 "+run.getDeleted());
		((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(run);
		if (run != null && !run.getDeleted()) updateGameAndUser(ctx, db, run);
	}
	
	private void updateGameAndUser(Context ctx, DBAdapter db, Run r) {
		Log.e("GAME", "in thread before 4 game update");
		Game g = r.getGame();
		PropertiesAdapter pa  = PropertiesAdapter.getInstance(ctx);
		if (g == null) {
			r = RunClient.getRunClient().getRun(r.getRunId(), pa.getFusionAuthToken());
			Log.e("GAME", "in thread before 4 after game update");
			g = r.getGame();
		}
		if (g != null) {
			db.getGameAdapter().insert(g);
		}
		User u = UserClient.getUserClient().getUser(pa.getFusionAuthToken(), r.getRunId(), pa.getUsername());
		db.getRunAdapter().insert(r, u.getRoles());
	}
	
	protected void updateActivities(Context ctx) {
		Log.e("GAME", "in thread 5  update activities");
		updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName());
	}
	
	
}
