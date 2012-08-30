package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.android.sync.MyActionsSyncronizer;
import org.celstec.arlearn2.beans.run.Action;

import android.content.Intent;

public class PublishActionTask extends RestInvocation {

	public PublishActionTask(IGeneralActivity activity) {
		super(activity);
	}
	
	
	@Override
	protected Void doInBackground(Object... params) {
		
		Action action = (Action) params[0];
		DBAdapter db = new DBAdapter(callingActivity.getMenuHandler().getContext().getApplicationContext());
		db.openForWrite();
		((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).insert(action);
		db.close();
		if (MyActionsSyncronizer.getInstance() != null) MyActionsSyncronizer.getInstance().run();
		Intent intent = new Intent(callingActivity.getMenuHandler().getContext(), BackgroundService.class);
		intent.putExtra("depCheck", true);
		callingActivity.getMenuHandler().getContext().startService(intent);
		return null;
	}

}
