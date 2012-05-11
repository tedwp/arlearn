package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.service.NotificationService;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;
import org.celstec.arlearn2.android.sync.RunSyncroniser;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;

import android.content.Context;
import android.widget.Toast;

public class RefreshTask extends RestInvocation {

	public RefreshTask(IGeneralActivity activity) {
		super(activity);
	}
	
	
	@Override
	protected Void doInBackground(Object... params) {
		Context ctx = callingActivity.getMenuHandler().getContext();
		PropertiesAdapter pa = callingActivity.getMenuHandler().getPropertiesAdapter();
		publishProgress("syncing runs");

		RunList rl = RunSyncroniser.syncronizeRuns(callingActivity.getMenuHandler().getContext(),pa);
		if (rl == null) return null;
		if (rl.getRuns() == null || rl.getRuns().isEmpty()) return null;
		for (Run r: rl.getRuns()){
			GeneralItemsSyncroniser.syncronizeItems(ctx, pa, r.getRunId());
			publishProgress("syncing gen items");
		}
		
		return null;
	}
	@Override
	 protected void onProgressUpdate(String... progress) {
		Toast.makeText(callingActivity.getMenuHandler().getContext(), progress[0], Toast.LENGTH_SHORT).show();
     }
}
