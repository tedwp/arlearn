package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;

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

public class RunReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			RunModification bean = (RunModification) extras.getSerializable("bean");
			if (bean != null) {
				process(context, bean);
			} 
		}else {
			syncronizeRuns(context);
		}
	}

	private void process(Context ctx, RunModification rm) {
		databaseOperations(ctx, rm);
		updateActivities(ctx);
	}
	
	private void databaseOperations(Context ctx, RunModification rm) {
		System.out.println("inserting new Run "+Run.class.getName());
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		switch (rm.getModificationType()) {
		case RunModification.CREATED:
			runToDb(db, rm.getRun(), ctx);
			break;
		case RunModification.DELETED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			break;
		case RunModification.ALTERED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
			updateGameAndUser(ctx, db, rm.getRun());
			break;
		default:
			break;
		}
		db.close();
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
		((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(run);
		updateGameAndUser(ctx, db, run);
	}
	
	private void updateGameAndUser(Context ctx, DBAdapter db, Run r) {
		Game g = r.getGame();
		PropertiesAdapter pa  = PropertiesAdapter.getInstance(ctx);
		if (g == null) {
			r = RunClient.getRunClient().getRun(r.getRunId(), pa.getFusionAuthToken());
			g = r.getGame();
		}
		if (g != null) {
			db.getGameAdapter().insert(g);
		}
		User u = UserClient.getUserClient().getUser(pa.getFusionAuthToken(), r.getRunId(), pa.getUsername());
		db.getRunAdapter().insert(r, u.getRoles());
	}
	
	private void updateActivities(Context ctx) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);
		ctx.sendBroadcast(updateIntent);
	}
	
	private void setStatusToLogout(Context context) {
		PropertiesAdapter.getInstance(context).disAuthenticate();
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
		context.sendBroadcast(updateIntent);
	}
}
