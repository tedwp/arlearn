package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.android.db.beans.Run;
import org.celstec.arlearn2.android.sync.RunSyncroniser;
import org.celstec.arlearn2.beans.run.Run;
//import org.celstec.arlearn2.genericBeans.Run;
//import org.celstec.arlearn2.genericBeans.GeneralItem;

import android.content.Context;
import android.util.Log;

public class GetFusionExcursions extends RestInvocation {
	Run[] runs =  null;
	org.celstec.arlearn2.android.db.notificationbeans.Run runNotification = null;
	public GetFusionExcursions(IGeneralActivity activity) {
		super(activity);
	}

	public GetFusionExcursions(IGeneralActivity activity, org.celstec.arlearn2.android.db.notificationbeans.Run runNotification) {
		super(activity);
		this.runNotification = runNotification;
	}
	
	protected Void doInBackground(Object... params) {
		Context ctx = (Context) callingActivity;
		
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		processRunNotification(ctx, db);
		RunAdapter adapter = (RunAdapter) db.table(DBAdapter.RUN_ADAPTER);
		runs = (Run[]) adapter.query();
		//Log.e("runs", "title "+runs[0].getTitle());
		db.close();
		
		return null;
	}
	
	private void processRunNotification(Context ctx, DBAdapter db) {
		if (runNotification != null) {
			if (runNotification.getCreated()!=null && runNotification.getCreated()) {
				RunSyncroniser rs = new RunSyncroniser(ctx);
				rs.runAuthenticated();
			}
			if (runNotification.getDeleted()!=null && runNotification.getDeleted()) {
				db.table(DBAdapter.RUN_ADAPTER).delete(runNotification.getRunId());
			}
		}
	}
	
	protected void onPostExecute(Void result) {
		callingActivity.notifyTaskFinished();
		super.onPostExecute(result);
	}
	
	public Run[] getResult() {
		return runs;
	}
	
	public Run[] getExcursionsFromDatabase() {
		Run[] result;
		Context ctx = (Context) callingActivity;
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		result = (Run[]) ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).query();
		db.close();
		return result;
	}

}
