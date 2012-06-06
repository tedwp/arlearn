package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RunReceiver  extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			RunModification bean = (RunModification) extras.getSerializable("bean");
			if (bean != null) {
				System.out.println("in GeneralItemReceiver before store");
				process(context, bean);
				
				Intent updateIntent = new Intent();
				updateIntent.setAction("org.celstec.arlearn.updateActivities");
				updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);

				context.sendBroadcast(updateIntent);
			}
		}
	}

	private void process(Context ctx, RunModification rm) {
		System.out.println("inserting new Run "+Run.class.getName());
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		switch (rm.getModificationType()) {
		case RunModification.CREATED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
			break;
		case RunModification.DELETED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			break;
		case RunModification.ALTERED:
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
			break;
		default:
			break;
		}
		db.close();


		Intent intent = new Intent(Run.class.getName());
		intent.putExtra("bean", rm);
		ctx.sendBroadcast(intent);
	}
}
